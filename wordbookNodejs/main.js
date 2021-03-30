var mysql = require('mysql');
var express = require('express');
var bodyParser = require('body-parser');
var app = express(); 

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));


app.listen(3000, '0.0.0.0', function () {
    console.log('서버 실행 중...');
});

//db 연결
var connection = mysql.createConnection({
    host: "192.168.43.178",
    user: "root",
    database: "wordbook1",
    password: "1234",
    port: 3306
});
// db연결- 다중쿼리시 사용
var multiconnection = mysql.createConnection({
    host: "192.168.43.178",
    user: "root",
    database: "wordbook1",
    password: "1234",
    port: 3306,
    multipleStatements: true
});

//내 스터디 목록  - 요청 변수는 UserUid 하나  응답 변수는 code, message, studylist(User가 속한 스터디 정보 배열)로 구성
app.post('/user/study', function (req, res) {
    console.log("내 스터디 목록\n"+req.body);  //
    var UserUid = req.body.UserUid; // 요청 변수 하나

    // 유저의 스터디 정보를 불러오는 select sql문 -응답 클래스studylist- 배열(Rid, room_name,host) 구성
    var sql = 'SELECT Rid,study.room_name,study.host,study.notice,studyrelation.msg FROM study  JOIN studyrelation USING(Rid) WHERE studyrelation.Uid=?;';
    var params = UserUid;

    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    connection.query(sql, UserUid, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
            resultCode=400;
            message='스터디 목록을 불러오던중 오류 발생';
        } else {
            resultCode = 200;
            message = '스터디 목록을 성공적으로 불러왔습니다.';
        }
        res.json({
            'code': resultCode,
            'message': message,
            'studylist':result
        });
    });
});

// 스터디 msg 가져오기 요청변수 Rid , 응답변수 code ,message, msglist(username, msg 배열)
app.post('/study/getmsg', function (req, res) {
    console.log("스터디 getmsg\n"+req.body);  //
    var Rid = req.body.Rid; // 요청 변수 하나
    var containInvited =req.body.flag


    // 유저의 스터디 정보를 불러오는 select sql문 -응답 클래스studylist- 배열(Rid, room_name,host) 구성
    var sql = 'SELECT username,Uid, msg FROM studyrelation JOIN user USING (Uid) WHERE Rid = ? AND msg IS NOT null;'
    var sql2 = 'SELECT username,Uid, msg FROM studyrelation JOIN user USING (Uid) WHERE Rid = ?'
    if(containInvited){
        sql=sql2
    }
   

    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    connection.query(sql, Rid, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
            resultCode=400;
            message='스터디 msg 가져오던 중 오류 발생';
        } else {
            resultCode = 200;
            message = '스터디 msg 성공적으로 불러왔습니다.';
        }

        res.json({
            'code': resultCode,
            'message': message,
            'msglist':result
        });
    });
});


// 스터디 외운단어 랭크 가져오기  - 요청 변수는 bookid   응답 변수는 code, message,result (Username, count)배열 구성
app.post('/study/getrank', function (req, res) {
    console.log("스터디랭크\n"+req.body);
    var bookid = req.body.bookid;

    // 단어장을 추가 후 추가된 단어장의 PK bookid출력
    var sql = 'SELECT username,COUNT(*) AS count  FROM checkword JOIN user USING (uid) WHERE bookid= 34 GROUP by Uid ORDER BY COUNT DESC LIMIT 3;'

    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    connection.query(sql, bookid, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
            resultCode=400;
            message='스터디 랭크를 가져오던 중 오류 발생';
        } else {
            resultCode = 200;
            message = '스터디랭크를 성공적으로 가져옴생성';
            
        }
        res.json({
            'code': resultCode,
            'message': message,
            'rank' : result
        });
    });
});

// 스터디 추가  - 요청 변수는 room_name,host  응답 변수는 code, message, rid 구성
app.post('/user/addstudy', function (req, res) {
    console.log("스터디 생성\n"+req.body);
    var room_name = req.body.room_name;
    var host=req.body.host;
    var notice="아직 공지를 생성하지 않았습니다."

    // 단어장을 추가 후 추가된 단어장의 PK bookid출력
    var sql = 'INSERT INTO study (room_name,host,notice) VALUES(?,?,?);';
    var params = [room_name,host,notice];
    
    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var Rid = -1;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
            resultCode=400;
            message='스터디를 생성하던 중 오류 발생';
        } else {
            resultCode = 200;
            message = '스터디를 성공적으로 생성';
            Rid=result.insertId            
            
        }
        res.json({
            'code': resultCode,
            'message': message,
            'Rid': Rid
        });
    });
});



// 스터디에 사람 추가  - 요청 변수는 Rid,Uid,ishost  응답 변수는 code, message(Normalresponse) 구성
app.post('/study/participate', function (req, res) {
    console.log("스터디 사람 추가\n"+req.body);
    var Rid = req.body.Rid;
    var Uid=req.body.Uid;
    var msg= req.body.ishost ? "" :null // msg로 스터디에 수락 여부를 결정하기 때문에 

    // 단어장을 추가 후 추가된 단어장의 PK bookid출력
    var sql = 'INSERT INTO studyrelation (Rid,Uid,msg) VALUES(?,?,?);';
    var params = [Rid,Uid,msg];
    
    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var Rid = -1;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
            resultCode=400;
            message='스터디에 추가하던 중 오류 발생';
        } else {
            resultCode = 200;
            message = '스터디에 추가 성공';
            
        }
        res.json({
            'code': resultCode,
            'message': message
        });
    });
});

// 스터디에 notice수정 , 요청 변수는 Rid,notice  응답 변수는 code, message(Normalresponse) 구성
app.post('/study/updatenotice', function (req, res) {
    console.log("스터디 notice 업데이트\n"+req.body);
    var Rid = req.body.Rid;
    var notice= req.body.notice  //  

    // 단어장을 추가 후 추가된 단어장의 PK bookid출력
    var sql = 'UPDATE study SET notice= ? WHERE Rid= ?;';
    var params = [notice,Rid];
    
    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
            resultCode=400;
            message='스터디 notice 업데이트중 에러 발생';
        } else {
            resultCode = 200;
            message = '스터디 notice 업데이트 성공';
            
        }
        res.json({
            'code': resultCode,
            'message': message
        });
    });
});



// 스터디에 유저 msg변경 , null->""로 스터디 초대 수락에도 사용  - 요청 변수는 Rid,Uid,msg  응답 변수는 code, message(Normalresponse) 구성
app.post('/study/updatemsg', function (req, res) {
    console.log("스터디 msg 업데이트\n"+req.body);
    var Rid = req.body.Rid;
    var Uid=req.body.Uid;
    var msg= req.body.msg  //  

    // 단어장을 추가 후 추가된 단어장의 PK bookid출력
    var sql = 'UPDATE studyrelation SET msg=? WHERE Rid=? AND Uid = ?;';
    var params = [msg,Rid,Uid];
    
    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
            resultCode=400;
            message='스터디 msg 업데이트중 에러 발생';
        } else {
            resultCode = 200;
            message = '스터디 msg 업데이트 성공';
            
        }
        res.json({
            'code': resultCode,
            'message': message
        });
    });
});

// 스터디 초대 거절- 스터디 탈퇴에도 사용  요청 변수는 Rid,Uid  응답 변수는 code, message(Normalresponse) 구성
app.post('/study/reject', function (req, res) {
    console.log("스터디 초대 거절\n"+req.body);
    var Rid = req.body.Rid;
    var Uid=req.body.Uid; 

    // 
    var sql = 'DELETE FROM studyrelation  WHERE Rid= ? AND Uid=?; DELETE FROM checkword WHERE Uid = ? AND bookid= (SELECT bookid FROM wordbook WHERE Rid=?);';
    var params = [Rid,Uid,Uid,Rid];
    
    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    multiconnection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
            resultCode=400;
            message='스터디 초대 거절 중 에러 발생';
        } else {
            resultCode = 200;
            message = '스터디 초대 거절 완료';
            
        }
        res.json({
            'code': resultCode,
            'message': message
        });
    });
});



//스터디 단어장-  - 요청 변수는 Rid 하나  응답 변수는 code, message, bookid 구성
app.post('/study/wordbook', function (req, res) {
    console.log("스터디단어장\n"+req.body);  //
    var Rid = req.body.Rid; // 요청 변수 하나

    // 유저의 단어장 목록을 받아오는 select sql문 -응답 클래스-booklist 배열(bookid,bookname) 구성
    var sql = 'SELECT bookid from Wordbook where Rid = ?';
    var params = Rid;
    
    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';
        var bookid=-1;

        if (err | result.length != 1) {
            console.log(err);
            resultCode=400;
            message='스터디의 단어장 id를 불러오던중 오류 발생';
        } else {
            resultCode = 200;
            bookid=result[0].bookid
            message = '스터디의 단어장 id를 불러왔습니다.';
        }

        res.json({
            'code': resultCode,
            'message': message,
            'bookid':bookid
        });
    });
});


// 단어장 추가  - 요청 변수는 Rid,Uid,bookname  응답 변수는 code, message, bookid로 구성
app.post('/user/addwordbook', function (req, res) {
    console.log("내 단어장 추가\n"+req.body);
    var Uid = req.body.Uid;
    var Rid=req.body.Rid;
    var bookname=req.body.bookname;

    // 단어장을 추가 후 추가된 단어장의 PK bookid출력
    var sql = 'INSERT INTO wordbook (Rid,Uid,bookname) VALUES(?,?,?);';
    var params = [Rid,Uid,bookname];
    
    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var bookid = -1;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
            resultCode=400;
            message='단어장 목록을 불러오던중 오류 발생';
        } else {
            resultCode = 200;
            message = '단어장 목록을 성공적으로 불러왔습니다.';
            bookid=result.insertId            
            
        }
        res.json({
            'code': resultCode,
            'message': message,
            'bookid': bookid
        });
    });
});

//내 단어장 목록  - 요청 변수는 UserUid 하나  응답 변수는 code, message, booklist(User의 단어장들)로 구성
app.post('/user/wordbook', function (req, res) {
    console.log("내 단어장 목록\n"+req.body);  //
    var UserUid = req.body.UserUid; // 요청 변수 하나

    // 유저의 단어장 목록을 받아오는 select sql문 -응답 클래스-booklist 배열(bookid,bookname) 구성
    var sql = 'SELECT bookid,Rid,Uid,bookname from Wordbook where Uid = 4 and Rid IS null';
    var params = UserUid;
    
    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
            resultCode=400;
            message='단어장 목록을 불러오던중 오류 발생';
        } else {
            resultCode = 200;
            message = '단어장 목록을 성공적으로 불러왔습니다.';
        }

        res.json({
            'code': resultCode,
            'message': message,
            'booklist':result
        });
    });
});


//단어장 단어 목록  - 요청 변수는 bookId 하나  응답 변수는 code, message, wordlist(단어장의 단어들)로 구성
app.post('/user/wordbook/word', function (req, res) {
    console.log("단어장 단어 목록 출력\n"+req.body);  //
    var bookId = req.body.bookId; // 요청 변수 하나

    // 단어장의 단어 목록을 받아오는 select sql문 -응답 클래스wordlist- 배열(Wordid,word_eng,mean) 구성
    var sql = 'SELECT Wordid,word_eng,mean from Word where bookid=?';
    var params = bookId;

    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
            resultCode=400;
            message='단어 목록을 불러오던중 오류 발생';
        } else {
            resultCode = 200;
            message = '단어 목록을 성공적으로 불러왔습니다.';
        }
        res.json({
            'code': resultCode,
            'message': message,
            'wordlist':result
        });
    });
});

//단어장에 단어 추가 - 요청 변수는 word_eng,mean,bookid  응답 변수는 code, message (Normalresponse)로 구성
app.post('/user/wordbook/addword', function (req, res) {
    console.log("단어 추가 수행\n"+req.body);  //
    var word_eng = req.body.word_eng; // 
    var mean = req.body.mean; // 
    var bookid = req.body.bookid; // 

    // 단어장에 단어 추가 INSERT sql문 -응답 클래스 normal- 구성
    var sql = 'INSERT INTO word (word_eng,mean,bookid) VALUES(?,?,?)';
    var params = [word_eng,mean,bookid];

    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
            resultCode=400;
            message='단어를 추가하던중 오류 발생';
        } else {
            resultCode = 200;
            message = '단어를 성공적으로 추가';
        }
        res.json({
            'code': resultCode,
            'message': message,
        });
    });
});

//단어장에 단어 수정- 요청 변수는 word_eng,mean,bookid,wordid  응답 변수는 code, message (Normalresponse)로 구성
app.post('/user/wordbook/editword', function (req, res) {
    console.log("단어 수정 수행\n"+req.body);  //
    var word_eng = req.body.word_eng; // 
    var mean = req.body.mean; // 
    var bookid = req.body.bookid; // 
    var wordid = req.body.wordid; // 

    // 값 갱신 update sql문 -응답 클래스 normal 구성
    var sql = 'UPDATE word SET word_eng= ? ,mean= ? WHERE bookid= ?  and wordid= ?';
    var params = [word_eng,mean,bookid,wordid];

    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
            resultCode=400;
            message='단어를 수정하던중 오류 발생';
        } else {
            resultCode = 200;
            message = '단어를 성공적으로 수정';
        }
        res.json({
            'code': resultCode,
            'message': message,
        });
    });
});

//단어장에 단어 삭제- 요청 변수는 bookid,wordid  응답 변수는 code, message (Normalresponse)로 구성
app.post('/user/wordbook/delword', function (req, res) {
    console.log("단어 삭제 수행\n"+req.body);  //
    
    var bookid = req.body.bookid; // 
    var wordid = req.body.wordid; // 

    // 값 갱신 update sql문 -응답 클래스 normal 구성
    var sql = 'DELETE from word WHERE bookid=? AND wordid=?';
    var params = [bookid,wordid];

    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
            resultCode=400;
            message='단어를 삭제하던중 오류 발생';
        } else {
            resultCode = 200;
            message = '단어를 성공적으로 삭제';
        }
        res.json({
            'code': resultCode,
            'message': message,
        });
    });
});


//외운 단어 목록  - 요청 변수는 bookId,Uid  응답 변수는 code, message, wordlist(단어장의 단어들)로 구성
app.post('/user/wordbook/getcheckword', function (req, res) {
    console.log(req.body);  //
    var bookId = req.body.bookId;
    var Uid = req.body.Uid;

    
    // 해당 단어장, 유저의 외운 단어들의 Wordid를 가져옴
    var sql = 'SELECT Wordid FROM checkword WHERE uid = ? AND bookid =?;';
    var params =[Uid, bookId];

    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
            resultCode=400;
            message='암기한 단어 목록을 불러오던중 오류 발생';
        } else {
            resultCode = 200;
            message = '암기한 단어 목록을 성공적으로 불러왔습니다.';
        }
        res.json({
            'code': resultCode,
            'message': message,
            'memoword':result
        });
    });
});

//외운 단어 체크  요청 변수는 bookId,Uid,Wordid  응답 변수는 code, message (Normalresponse)로 구성
app.post('/user/wordbook/checkword', function (req, res) {
    console.log(req.body);  //
    var bookId = req.body.bookId;
    var Uid = req.body.Uid;
    var Wordid=req.body.Wordid;
    
    // 해당 단어장, 유저의 외운 단어들의 Wordid를 가져옴
    var sql = 'INSERT INTO checkword (uid,Wordid,bookid) VALUES(?,?,?)';
    var params =[Uid,Wordid ,bookId];

    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
            resultCode=400;
            message='암기한 단어를 체크 하던 중 오류발생';
        } else {
            resultCode = 200;
            message = '암기한 단어 체크 성공';
        }
        res.json({
            'code': resultCode,
            'message': message
        });
    });
});

//외운 단어 체크 해제  요청 변수는 bookId,Uid,Wordid  응답 변수는 code, message (Normalresponse)로 구성
app.post('/user/wordbook/uncheckword', function (req, res) {
    console.log(req.body);  //
    var bookId = req.body.bookId;
    var Uid = req.body.Uid;
    var Wordid=req.body.Wordid;
    
    // 해당 단어장, 유저의 외운 단어들의 Wordid를 가져옴
    var sql = 'DELETE FROM checkword WHERE Wordid= ? AND uid =? AND bookid=? ';
    var params =[Wordid,Uid ,bookId];

    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
            resultCode=400;
            message='암기한 단어를 체크해제 하던 중 오류발생';
        } else {
            resultCode = 200;
            message = '암기한 단어 체크해제 성공';
        }
        res.json({
            'code': resultCode,
            'message': message
        });
    });
});



// 회원 가입  - 요청 변수는 username,email,password,picture - 응답 변수는 code, message
app.post('/user/join', function (req, res) {
    console.log(req.body);
    var username = req.body.username;
    var email = req.body.email;
    var password = req.body.password;
    var picture=req.body.picture;

    // 삽입을 수행하는 sql문.
    var sql = 'INSERT INTO user (username, email, password, picture) VALUES (?, ?, ?, ?)';
    var params = [username,email, password, picture];

    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = '회원가입에 성공했습니다.';
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    });
});


// 로그인   - 요청 변수는 email, password - 응답 변수는 code, message, Uid 로 구성
app.post('/user/login', function (req, res) {
    var email = req.body.email;
    var password = req.body.password;
    var sql = 'select * from User where email = ?';
    connection.query(sql,email, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';
        var userId="error"
        if (err) {
            console.log(err);
        } else {
            if (result.length === 0) {
                resultCode = 204;
                message = '존재하지 않는 계정입니다!';
            } else if (password !== result[0].password) {
                resultCode = 204;
                message = '비밀번호가 틀렸습니다!';
            } else {
                resultCode = 200;
                message = '로그인 성공! ' + result[0].username + '님 환영합니다!';
                userId=result[0].Uid;
            }
        }
        res.json({
            'code': resultCode,
            'message': message,
            'Uid' : userId
        });
    })
});

// 이메일로 uid   - 요청 변수는 email  - 응답 변수는 code, message, Uid 로 구성
app.post('/user/getuid', function (req, res) {
    var email = req.body.email;
    
    var sql = 'select * from User where email = ?';
    connection.query(sql,email, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';
        var userId="error"
        if (err) {
            console.log(err);
        } else {
            if (result.length === 0) {
                resultCode = 204;
                message = '존재하지 않는 계정입니다!';
            } else {
                resultCode = 200;
                message = 'uid를 성공적으로 가져왔습니다.';
                userId=result[0].Uid;
            }
        }
        res.json({
            'code': resultCode,
            'message': message,
            'Uid' : userId
        });
    })
});

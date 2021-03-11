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

var multiconnection = mysql.createConnection({
    host: "192.168.43.178",
    user: "root",
    database: "wordbook1",
    password: "1234",
    port: 3306,
    multipleStatements: true
});

/*

insert into Study (room_name, host) VALUES (?,?)
insert into Studyrelation ()
insert into Word select word_eng,mean,newbookid from word where bookid=oldbookid;  //

*/

//내 스터디 목록  - 요청 변수는 UserUid 하나  응답 변수는 code, message, studylist(User가 속한 스터디 정보 배열)로 구성
app.post('/user/study', function (req, res) {
    console.log("내 스터디 목록\n"+req.body);  //
    var UserUid = req.body.UserUid; // 요청 변수 하나

    // 유저의 스터디 정보를 불러오는 select sql문 -응답 클래스studylist- 배열(Rid, room_name,host) 구성
    var sql = 'SELECT Rid,room_name,host from Study where Rid =(SELECT Rid from Studyrelation where Uid=?)';
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

//내 단어장 목록  - 요청 변수는 UserUid 하나  응답 변수는 code, message, booklist(User의 단어장들)로 구성
app.post('/user/wordbook', function (req, res) {
    console.log("내 단어장 목록\n"+req.body);  //
    var UserUid = req.body.UserUid; // 요청 변수 하나

    // 유저의 단어장 목록을 받아오는 select sql문 -응답 클래스-booklist 배열(bookid,bookname) 구성
    var sql = 'SELECT bookid,Rid,Uid,bookname from Wordbook where Uid = ? ';
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
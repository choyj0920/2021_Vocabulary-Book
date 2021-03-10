SELECT * FROM user;

INSERT INTO user (username, email, password, picture) VALUES ("uz","choyj0920@naver.com", "123qwe","null");

SELECT * FROM user WHERE email="choyj0920@naver.com";

SELECT * FROM wordbook;

INSERT INTO wordbook (Rid,Uid,bookname) VALUES(NULL,4,"단어장이에여!");

INSERT INTO wordbook (Rid,Uid,bookname) VALUES(NULL,4,"단어장2");
INSERT INTO wordbook (Rid,Uid,bookname) VALUES(NULL,4,"단어장3");
INSERT INTO wordbook (Rid,Uid,bookname) VALUES(NULL,4,"단어장4");

SLECT bookid,bookname from Wordbook where Uid = 4;

SELECT * FROM wordword;

INSERT INTO word (word_eng,mean,bookid) VALUES("hayoung","하영",1);
INSERT INTO word (word_eng,mean,bookid) VALUES("yoonjik","윤직",1);
INSERT INTO word (word_eng,mean,bookid) VALUES("sample","샘플",1);
INSERT INTO word (word_eng,mean,bookid) VALUES("word1","단어1",1);
INSERT INTO word (word_eng,mean,bookid) VALUES("word2","단어2",1);
INSERT INTO word (word_eng,mean,bookid) VALUES("word3","단어3",1);

#SELECT Wordid,word_eng,mean from Word where bookid=?

INSERT INTO checkword (uid,Wordid,bookid) VALUES(4,3,1);
INSERT INTO checkword (uid,Wordid,bookid) VALUES(4,5,1);
INSERT INTO checkword (uid,Wordid,bookid) VALUES(4,6,1);

SELECT * FROM checkword;

SELECT Wordid FROM checkword WHERE uid = 4 AND bookid =1;

DELETE FROM checkword WHERE Wordid= 6 AND uid =4 AND bookid=1;

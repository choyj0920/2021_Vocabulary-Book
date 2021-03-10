CREATE TABLE `Study` (
	`Rid`	Int	primary key auto_increment,
	`room_name`	varchar(50)	NULL,
	`host`	varchar(50)	NULL
);

CREATE TABLE `User` (
	`Uid`	Int	primary key auto_increment,
	`username`	varchar(50)	NULL,
	`email`	varchar(50)	NULL,
	`password`	varchar(50)	NULL,
	`picture`	varchar(50)	NULL
);

CREATE TABLE `Studyrelation` (
	`Uid`	Int	NOT NULL,
	`Rid`	Int	NOT NULL
);

CREATE TABLE `Wordbook` (
	`bookid`	Int	primary key auto_increment,
	`Rid`	Int	NULL,
	`Uid`	Int	NULL,
	`bookname`	varchar(50)	NULL
);

CREATE TABLE `Word` (
	`Wordid`	Int	primary key auto_increment,
	`word_eng`	varchar(50)	NULL,
	`mean`	varchar(50)	NULL,
	`bookid`	Int	NOT NULL
);

CREATE TABLE `Checkword` (
	`Uid`	Int	NOT NULL,
	`Wordid`	Int	NOT NULL,
	`bookid`	Int	NOT NULL
);

ALTER TABLE `Study` ADD CONSTRAINT `PK_STUDY` PRIMARY KEY (
	`Rid`
);

ALTER TABLE `User` ADD CONSTRAINT `PK_USER` PRIMARY KEY (
	`Uid`
);

ALTER TABLE `Studyrelation` ADD CONSTRAINT `PK_STUDYRELATION` PRIMARY KEY (
	`Uid`,
	`Rid`
);

ALTER TABLE `Wordbook` ADD CONSTRAINT `PK_WORDBOOK` PRIMARY KEY (
	`bookid`
);

ALTER TABLE `Word` ADD CONSTRAINT `PK_WORD` PRIMARY KEY (
	`Wordid`
);

ALTER TABLE `Studyrelation` ADD CONSTRAINT `FK_User_TO_Studyrelation_1` FOREIGN KEY (
	`Uid`
)
REFERENCES `User` (
	`Uid`
);

ALTER TABLE `Studyrelation` ADD CONSTRAINT `FK_Study_TO_Studyrelation_1` FOREIGN KEY (
	`Rid`
)
REFERENCES `Study` (
	`Rid`
);

ALTER TABLE `Wordbook` ADD CONSTRAINT `FK_Study_TO_Wordbook_1` FOREIGN KEY (
	`Rid`
)
REFERENCES `Study` (
	`Rid`
);

ALTER TABLE `Wordbook` ADD CONSTRAINT `FK_User_TO_Wordbook_1` FOREIGN KEY (
	`Uid`
)
REFERENCES `User` (
	`Uid`
);

ALTER TABLE `Word` ADD CONSTRAINT `FK_Wordbook_TO_Word_1` FOREIGN KEY (
	`bookid`
)
REFERENCES `Wordbook` (
	`bookid`
);

ALTER TABLE `Checkword` ADD CONSTRAINT `FK_User_TO_Checkword_1` FOREIGN KEY (
	`Uid`
)
REFERENCES `User` (
	`Uid`
);

ALTER TABLE `Checkword` ADD CONSTRAINT `FK_Word_TO_Checkword_1` FOREIGN KEY (
	`Wordid`
)
REFERENCES `Word` (
	`Wordid`
);

ALTER TABLE `Checkword` ADD CONSTRAINT `FK_Wordbook_TO_Checkword_1` FOREIGN KEY (
	`bookid`
)
REFERENCES `Wordbook` (
	`bookid`
);

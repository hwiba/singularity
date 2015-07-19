Create Table NOTES(
 noteId bigint PRIMARY KEY AUTO_INCREMENT,
 noteText text Not Null,
 createDate DATETIME DEFAULT CURRENT_TIMESTAMP,
 targetDate DATETIME Not Null,
 userId varchar(50),
 groupId char(5),
 commentCount int,
 Foreign Key(userId) REFERENCES USERS(userId),
 Foreign Key(groupId) REFERENCES GROUPS(groupId) on delete cascade
);
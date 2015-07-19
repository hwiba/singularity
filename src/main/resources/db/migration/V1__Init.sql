drop database stag;

drop user 'stag'@'localhost';

/* DB 생성 */
Create DATABASE stag DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

/* User 생성 및 DB권한 주기 */
Create User stag@'localhost' identified by 'stag';

Grant all privileges on stag.* to stag@'localhost' identified by 'stag';

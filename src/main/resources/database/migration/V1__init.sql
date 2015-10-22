drop database singularity;

drop user 'singularity'@'localhost';

/* DB 생성 */
Create DATABASE singularity DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

/* User 생성 및 DB권한 주기 */
Create User singularity@'localhost' identified by 'singularity';

Grant all privileges on singularity.* to singularity@'localhost' identified by 'singularity';
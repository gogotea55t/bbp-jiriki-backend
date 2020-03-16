CREATE TABLE IF NOT EXISTS jiriki_db.users (
	USER_ID VARCHAR(10) PRIMARY KEY,
	USER_NAME VARCHAR(15) NOT NULL
)/*! CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */;

CREATE TABLE IF NOT EXISTS jiriki_db.songs (
    SONG_ID VARCHAR(10) PRIMARY KEY,
    JIRIKI_RANK INT,
    SONG_NAME VARCHAR(60) NOT NULL,
    CONTRIBUTOR VARCHAR(20) NOT NULL,
    INSTRUMENT VARCHAR(15) NOT NULL
)/*! CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */;

CREATE TABLE IF NOT EXISTS jiriki_db.scores (
    SCORE_ID INT PRIMARY KEY AUTO_INCREMENT,
    SONGS_SONG_ID VARCHAR(10),
    USERS_USER_ID VARCHAR(10),
    SCORE TINYINT UNSIGNED,
    FOREIGN KEY (SONGS_SONG_ID) references jiriki_db.SONGS(SONG_ID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (USERS_USER_ID) references jiriki_db.USERS(USER_ID) ON UPDATE CASCADE ON DELETE CASCADE,
    UNIQUE SCORE_HOLDER_INDEX (SONGS_SONG_ID, USERS_USER_ID)
)/*! CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */;

CREATE TABLE IF NOT EXISTS jiriki_db.twitter_users (
    TWITTER_USER_ID VARCHAR(30) PRIMARY KEY,
    USERS_USER_ID VARCHAR(10),
    FOREIGN KEY (USERS_USER_ID) references jiriki_db.users(USER_ID) ON UPDATE CASCADE ON DELETE CASCADE
)/*! CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */;
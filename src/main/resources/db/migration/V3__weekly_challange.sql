CREATE TABLE IF NOT EXISTS WEEKLY_CHALLANGE (
	WEEKLY_CHALLANGE_ID INT PRIMARY KEY,
	SONGS_SONG_ID VARCHAR(10) NOT NULL,
	START_DATE DATE NOT NULL,
	END_DATE DATE NOT NULL
)/*! CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
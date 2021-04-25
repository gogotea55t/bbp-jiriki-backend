package io.github.gogotea55t.jiriki.domain.repository;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import io.github.gogotea55t.jiriki.domain.vo.song.SongId;
import io.github.gogotea55t.jiriki.domain.vo.user.UserId;

public class ScoreSqlBuilder {
  private static final String SONG_ALL_PARAMS =
      "so.SONG_ID,  so.JIRIKI_RANK, so.SONG_NAME, so.CONTRIBUTOR, so.INSTRUMENT";
  private static final String USER_ALL_PARAMS = "us.USER_ID, us.USER_NAME";
  private static final String SCORE_ALL_PARAMS =
      SONG_ALL_PARAMS
          + " , "
          + USER_ALL_PARAMS
          + " , sc.SCORE_ID, sc.SCORE, sc.CREATED_AT, sc.CREATED_BY, sc.UPDATED_AT, sc.UPDATED_BY ";
  private static final String STATS_ALL_PARAMS =
      "COUNT(SCORE = 100 or NULL) as gold, "
          + "COUNT((SCORE < 100 AND SCORE >= 90) OR NULL) as silver, "
          + "COUNT((SCORE < 90 AND SCORE >= 80) OR NULL) as bronze, "
          + "COUNT((SCORE < 80 AND SCORE >= 50) OR NULL) as blue, "
          + "COUNT((SCORE < 50) OR NULL) as gray";

  public static final String buildScoreFetchSql(
      @Param("userId") UserId userId, @Param("songId") SongId songId) {
    return new SQL() {
      {
        SELECT(SCORE_ALL_PARAMS);
        FROM("SCORES sc");
        INNER_JOIN("SONGS so ON sc.songs_song_id = so.song_id");
        INNER_JOIN("USERS us ON sc.users_user_id = us.user_id");
        WHERE("us.user_id = #{userId.value}");
        AND();
        WHERE("so.song_id = #{songId}");
      }
    }.toString();
  }

  public static final String buildScoreFetchBySongIdSql(SongId songId) {
    return new SQL() {
      {
        SELECT("us.user_name, sc.score");
        FROM("SCORES sc");
        INNER_JOIN("SONGS so ON sc.songs_song_id = so.song_id");
        INNER_JOIN("USERS us ON sc.users_user_id = us.user_id");
        WHERE("so.song_id = #{songId}");
        ORDER_BY("sc.score desc, us.user_id");
      }
    }.toString();
  }

  public static final String buildScoreFetchBySongIdSqlV2(SongId songId) {
    return new SQL() {
      {
        SELECT("us.user_id, us.user_name, sc.score");
        FROM("SCORES sc");
        INNER_JOIN("SONGS so ON sc.songs_song_id = so.song_id");
        INNER_JOIN("USERS us ON sc.users_user_id = us.user_id");
        WHERE("so.song_id = #{songId}");
        ORDER_BY("sc.score desc, us.user_id");
      }
    }.toString();
  }

  public static final String buildScoreStatSql(SongId songId) {
    return new SQL() {
      {
        SELECT(STATS_ALL_PARAMS);
        FROM("SCORES sc");
        WHERE("sc.songs_song_id = #{songId}");
      }
    }.toString();
  }

  public static final String buildScoreStatSqlGroupByJiriki(UserId userId) {
    return new SQL() {
      {
        SELECT(
            STATS_ALL_PARAMS + ", COUNT(SCORE IS NULL) - COUNT(SCORE) AS none , so.JIRIKI_RANK AS jiriki_rank");
        FROM("SONGS so");
        LEFT_OUTER_JOIN(
            "SCORES sc  ON sc.SONGS_SONG_ID = so.SONG_ID AND sc.USERS_USER_ID = #{userId.value}");
        GROUP_BY("so.JIRIKI_RANK");
        ORDER_BY("so.JIRIKI_RANK");
      }
    }.toString();
  }

  public static final String buildScoreStatSqlByUser(UserId userId) {
    return new SQL() {
      {
        SELECT(STATS_ALL_PARAMS + ", COUNT(SCORE IS NULL) - COUNT(SCORE) AS none");
        FROM("SONGS so");
        LEFT_OUTER_JOIN(
            "SCORES sc  ON sc.SONGS_SONG_ID = so.SONG_ID AND sc.USERS_USER_ID = #{userId.value}");
      }
    }.toString();
  }
}

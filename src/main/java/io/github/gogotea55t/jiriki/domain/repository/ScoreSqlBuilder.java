package io.github.gogotea55t.jiriki.domain.repository;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class ScoreSqlBuilder {
  private static final String SONG_ALL_PARAMS =
      "so.SONG_ID,  so.JIRIKI_RANK, so.SONG_NAME, so.CONTRIBUTOR, so.INSTRUMENT";
  private static final String USER_ALL_PARAMS = "us.USER_ID, us.USER_NAME";
  private static final String SCORE_ALL_PARAMS =
      SONG_ALL_PARAMS
          + " , "
          + USER_ALL_PARAMS
          + " , sc.SCORE_ID, sc.SCORE, sc.CREATED_AT, sc.CREATED_BY, sc.UPDATED_AT, sc.UPDATED_BY ";

  public static final String buildScoreFetchSql(
      @Param("userId") String userId, @Param("songId") String songId) {
    return new SQL() {
      {
        SELECT(SCORE_ALL_PARAMS);
        FROM("SCORES sc");
        INNER_JOIN("SONGS so ON sc.songs_song_id = so.song_id");
        INNER_JOIN("USERS us ON sc.users_user_id = us.user_id");
        WHERE("us.user_id = #{userId}");
        AND();
        WHERE("so.song_id = #{songId}");
      }
    }.toString();
  }

  public static final String buildScoreFetchBySongIdSql(String songId) {
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

  public static final String buildScoreFetchBySongIdSqlV2(String songId) {
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
}

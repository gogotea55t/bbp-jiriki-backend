package io.github.gogotea55t.jiriki.domain.repository;

import org.apache.ibatis.jdbc.SQL;

public class WeeklyChallangeSqlBuilder {
  private static final String WEEKLY_CHALLANGE_PARAMS =
      "wc.WEEKLY_CHALLANGE_ID, wc.START_DATE, wc.END_DATE, "
          + "so.SONG_ID, so.SONG_NAME, so.JIRIKI_RANK, so.CONTRIBUTOR, so.INSTRUMENT";

  public static final String buildWeeklyChallangeSql() {
    return new SQL() {
      {
        SELECT(WEEKLY_CHALLANGE_PARAMS);
        FROM("WEEKLY_CHALLANGE wc");
        INNER_JOIN("SONGS so ON wc.SONGS_SONG_ID = so.SONG_ID");
        ORDER_BY("wc.START_DATE desc");
        LIMIT(1);
      }
    }.toString();
  }

  public static final String buildfindAllSql() {
    return new SQL() {
      {
        SELECT(WEEKLY_CHALLANGE_PARAMS);
        FROM("WEEKLY_CHALLANGE wc");
        INNER_JOIN("SONGS so ON wc.SONGS_SONG_ID = so.SONG_ID");
        ORDER_BY("wc.START_DATE desc");
      }
    }.toString();
  }
}

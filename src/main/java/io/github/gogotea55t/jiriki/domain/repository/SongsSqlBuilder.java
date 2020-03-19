package io.github.gogotea55t.jiriki.domain.repository;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import io.github.gogotea55t.jiriki.domain.vo.JirikiRank;

public class SongsSqlBuilder {
  private static final String SONG_ALL_PARAMS =
      "so.SONG_ID, so.JIRIKI_RANK, so.SONG_NAME, so.CONTRIBUTOR, so.INSTRUMENT";

  public static String buildSongSearchSql(final Map<String, String> searchConditions) {
    return new SQL() {
      {
        SELECT(SONG_ALL_PARAMS);
        FROM("SONGS so");
        if (searchConditions.containsKey("name")) {
          WHERE("SONG_NAME like CONCAT('%', #{name}, '%')");
        } else if (searchConditions.containsKey("jiriki")) {
          WHERE(
              "JIRIKI_RANK = "
                  + JirikiRank.getJirikiRankFromRankName(searchConditions.get("jiriki"))
                      .getJirikiId());
        } else if (searchConditions.containsKey("contributor")) {
          WHERE("CONTRIBUTOR like CONCAT('%', #{contributor}, '%')");
        } else if (searchConditions.containsKey("instrument")) {
          WHERE("INSTRUMENT like CONCAT('%', #{instrument},'%')");
        }

        ORDER_BY("so.jiriki_Rank", "so.song_Id");
      }
    }.toString();
  }

  public static String buildAverageScoreSearchSql(final Map<String, String> searchConditions) {
    return new SQL() {
      {
        SELECT(SONG_ALL_PARAMS + ", ROUND(AVG(sc.score), 2) as score");
        FROM("SONGS so");
        LEFT_OUTER_JOIN("SCORES sc ON sc.songs_song_id = so.song_id");
        AND();
        if (searchConditions.containsKey("name")) {
          WHERE("SONG_NAME like CONCAT('%', #{name}, '%')");
        } else if (searchConditions.containsKey("jiriki")) {
          WHERE(
              "JIRIKI_RANK = "
                  + JirikiRank.getJirikiRankFromRankName(searchConditions.get("jiriki"))
                      .getJirikiId());
        } else if (searchConditions.containsKey("contributor")) {
          WHERE("CONTRIBUTOR like CONCAT('%', #{contributor}, '%')");
        } else if (searchConditions.containsKey("instrument")) {
          WHERE("INSTRUMENT like CONCAT('%', #{instrument},'%')");
        } else {
          WHERE("TRUE");
        }
        GROUP_BY("so.SONG_ID");
        ORDER_BY("so.jiriki_rank", "so.song_id");
      }
    }.toString();
  }

  public static String buildScoreSearchSql(
      final Map<String, String> searchConditions) {
    return new SQL() {
      {
        SELECT(SONG_ALL_PARAMS + ", sc.score as score");
        FROM("SONGS so");
        LEFT_OUTER_JOIN(
            "SCORES sc ON sc.songs_song_id = so.song_id AND sc.users_user_id = #{userId}");
        AND();
        if (searchConditions.containsKey("name")) {
          WHERE("SONG_NAME like CONCAT('%', #{name}, '%')");
        } else if (searchConditions.containsKey("jiriki")) {
          WHERE(
              "JIRIKI_RANK = "
                  + JirikiRank.getJirikiRankFromRankName(searchConditions.get("jiriki"))
                      .getJirikiId());
        } else if (searchConditions.containsKey("contributor")) {
          WHERE("CONTRIBUTOR like CONCAT('%', #{contributor}, '%')");
        } else if (searchConditions.containsKey("instrument")) {
          WHERE("INSTRUMENT like CONCAT('%', #{instrument},'%')");
        } else {
          WHERE("TRUE");
        }
        ORDER_BY("so.jiriki_rank", "so.song_id");
      }
    }.toString();
  }
}

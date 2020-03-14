package io.github.gogotea55t.jiriki.domain.repository;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import io.github.gogotea55t.jiriki.domain.vo.JirikiRank;

public class SongsSqlBuilder {
  private static final String SONG_ALL_PARAMS =
      "so.SONG_ID, so.JIRIKI_RANK, so.SONG_NAME, so.CONTRIBUTOR, so.INSTRUMENT";

  public static String buildSongSearchSql(final Map<String, String> searchConditions) {
    return new SQL() {
      {
        SELECT(SONG_ALL_PARAMS);
        FROM("songs so");
        if (searchConditions.containsKey("name")) {
          WHERE("SONG_NAME like '%" + searchConditions.get("name") + "%'");
        } else if (searchConditions.containsKey("jiriki")) {
          WHERE(
              "JIRIKI_RANK = "
                  + JirikiRank.getJirikiRankFromRankName(searchConditions.get("jiriki"))
                      .getJirikiId());
        } else if (searchConditions.containsKey("contributor")) {
          WHERE("CONTRIBUTOR like '%" + searchConditions.get("contributor") + "%'");
        } else if (searchConditions.containsKey("instrument")) {
          WHERE("INSTRUMENT like '%" + searchConditions.get("instrument") + "%'");
        }

        ORDER_BY("so.jiriki_Rank", "so.song_Id");
      }
    }.toString();
  }

  public static String buildAverageScoreSearchSql(final Map<String, String> searchConditions) {
    return new SQL() {
      {
        SELECT(SONG_ALL_PARAMS + ", AVG(sc.score) as score");
        FROM("songs so");
        LEFT_OUTER_JOIN("scores sc ON sc.songs_song_id = so.song_id");
        AND();
        if (searchConditions.containsKey("name")) {
          WHERE("SONG_NAME like '%" + searchConditions.get("name") + "%'");
        } else if (searchConditions.containsKey("jiriki")) {
          WHERE(
              "JIRIKI_RANK = "
                  + JirikiRank.getJirikiRankFromRankName(searchConditions.get("jiriki"))
                      .getJirikiId());
        } else if (searchConditions.containsKey("contributor")) {
          WHERE("CONTRIBUTOR like '%" + searchConditions.get("contributor") + "%'");
        } else if (searchConditions.containsKey("instrument")) {
          WHERE("INSTRUMENT like '%" + searchConditions.get("instrument") + "%'");
        } else {
          WHERE("TRUE");
        }
        GROUP_BY("so.SONG_ID");
        ORDER_BY("so.jiriki_rank", "so.song_id");
      }
    }.toString();
  }

  public static String buildScoreSearchSql(
      final String userId, final Map<String, String> searchConditions) {
    return new SQL() {
      {
        SELECT(SONG_ALL_PARAMS + ", sc.score as score");
        FROM("songs so");
        LEFT_OUTER_JOIN(
            "scores sc ON sc.songs_song_id = so.song_id AND sc.users_user_id = '" + userId + "'");
        AND();
        if (searchConditions.containsKey("name")) {
          WHERE("SONG_NAME like '%" + searchConditions.get("name") + "%'");
        } else if (searchConditions.containsKey("jiriki")) {
          WHERE(
              "JIRIKI_RANK = "
                  + JirikiRank.getJirikiRankFromRankName(searchConditions.get("jiriki"))
                      .getJirikiId());
        } else if (searchConditions.containsKey("contributor")) {
          WHERE("CONTRIBUTOR like '%" + searchConditions.get("contributor") + "%'");
        } else if (searchConditions.containsKey("instrument")) {
          WHERE("INSTRUMENT like '%" + searchConditions.get("instrument") + "%'");
        } else {
          WHERE("TRUE");
        }
        ORDER_BY("so.jiriki_rank", "so.song_id");
      }
    }.toString();
  }
}
package io.github.gogotea55t.jiriki.domain.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

import io.github.gogotea55t.jiriki.domain.entity.Scores;
import io.github.gogotea55t.jiriki.domain.response.Score4SongResponse;
import io.github.gogotea55t.jiriki.domain.response.Score4SongResponseV2;
import io.github.gogotea55t.jiriki.domain.response.StatisticResponse;
import io.github.gogotea55t.jiriki.domain.response.StatisticsResponseByJirikiRank;
import io.github.gogotea55t.jiriki.domain.vo.GenericStringValueHandler;
import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;
import io.github.gogotea55t.jiriki.domain.vo.user.UserId;
import io.github.gogotea55t.jiriki.domain.vo.user.UserName;

@Mapper
public interface ScoresRepository {
  @SelectProvider(type = ScoreSqlBuilder.class, method = "buildScoreFetchSql")
  public Optional<Scores> findByUsers_UserIdAndSongs_SongId(
      @Param("userId") UserId userId, @Param("songId") String songId);

  @Results({
    @Result(
        javaType = UserName.class,
        column = "userName",
        property = "userName",
        jdbcType = JdbcType.VARCHAR)
  })
  @SelectProvider(type = ScoreSqlBuilder.class, method = "buildScoreFetchBySongIdSql")
  public List<Score4SongResponse> findScoresBySongId(String songId);

  @Results({
    @Result(
        javaType = UserId.class,
        column = "USER_ID",
        property = "userId",
        jdbcType = JdbcType.VARCHAR),
    @Result(
        javaType = UserName.class,
        column = "USER_NAME",
        property = "userName",
        jdbcType = JdbcType.VARCHAR)
  })
  @SelectProvider(type = ScoreSqlBuilder.class, method = "buildScoreFetchBySongIdSqlV2")
  public List<Score4SongResponseV2> findScoresBySongIdV2(String songId);

  @SelectProvider(type = ScoreSqlBuilder.class, method = "buildScoreStatSql")
  public StatisticResponse getStatisticsOfSongs(String songId);

  @SelectProvider(type = ScoreSqlBuilder.class, method = "buildScoreStatSqlByUser")
  public StatisticResponse getStatisticsOfUsers(UserId userId);

  @SelectProvider(type = ScoreSqlBuilder.class, method = "buildScoreStatSqlGroupByJiriki")
  @Results(
      id = "statistics_jiriki",
      value = {
        @Result(column = "gold", property = "stats.gold"),
        @Result(column = "silver", property = "stats.silver"),
        @Result(column = "bronze", property = "stats.bronze"),
        @Result(column = "blue", property = "stats.blue"),
        @Result(column = "gray", property = "stats.gray"),
        @Result(column = "none", property = "stats.none"),
        @Result(column = "jiriki_rank", property = "jirikiRank"),
      })
  public List<StatisticsResponseByJirikiRank> getStatisticsOfUsersGroupByJirikiRank(UserId userId);

  @Insert(
      "<script>"
          + "INSERT INTO SCORES (USERS_USER_ID, SONGS_SONG_ID, SCORE) VALUES "
          + "<foreach item=\"scores\" collection=\"list\" separator=\",\"> "
          + "( #{scores.users.userId}, #{scores.songs.songId}, #{scores.score} )"
          + "</foreach>"
          + "</script>")
  public int saveAll(List<Scores> scores);

  @Insert(
      "INSERT INTO SCORES (USERS_USER_ID, SONGS_SONG_ID, SCORE, CREATED_BY, UPDATED_BY) VALUES ( #{users.userId}, #{songs.songId}, #{score}, #{createdBy}, #{updatedBy} )")
  public int save(Scores score);

  @Update(
      "UPDATE SCORES SET SCORE = #{score}, UPDATED_BY = #{updatedBy} WHERE USERS_USER_ID = #{users.userId} AND SONGS_SONG_ID = #{songs.songId}")
  public int update(Scores score);

  @Select("SELECT COUNT(*) FROM SCORES")
  public int count();

  @Select("DELETE FROM SCORES WHERE USERS_USER_ID = #{userId} AND SONGS_SONG_ID = #{songId}")
  public void delete(@Param("songId") String songId, @Param("userId") UserId userId);

  @Delete("DELETE FROM SCORES")
  public int deleteAll();
}

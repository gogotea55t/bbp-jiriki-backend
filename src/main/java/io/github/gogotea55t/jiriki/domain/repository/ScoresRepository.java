package io.github.gogotea55t.jiriki.domain.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import io.github.gogotea55t.jiriki.domain.entity.Scores;
import io.github.gogotea55t.jiriki.domain.response.Score4SongResponse;
import io.github.gogotea55t.jiriki.domain.response.Score4SongResponseV2;
import io.github.gogotea55t.jiriki.domain.response.StatisticResponse;

@Mapper
public interface ScoresRepository {
  @SelectProvider(type = ScoreSqlBuilder.class, method = "buildScoreFetchSql")
  public Optional<Scores> findByUsers_UserIdAndSongs_SongId(@Param("userId") String userId, @Param("songId") String songId);

  @SelectProvider(type = ScoreSqlBuilder.class, method = "buildScoreFetchBySongIdSql")
  public List<Score4SongResponse> findScoresBySongId(String songId);

  @SelectProvider(type = ScoreSqlBuilder.class, method = "buildScoreFetchBySongIdSqlV2")
  public List<Score4SongResponseV2> findScoresBySongIdV2(String songId);
  
  @SelectProvider(type = ScoreSqlBuilder.class, method = "buildScoreStatSql")
  public StatisticResponse getStatisticsOfSongs(String songId);
  
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
  
  @Update("UPDATE SCORES SET SCORE = #{score}, UPDATED_BY = #{updatedBy} WHERE USERS_USER_ID = #{users.userId} AND SONGS_SONG_ID = #{songs.songId}")
  public int update(Scores score);
  
  
  @Select("SELECT COUNT(*) FROM SCORES")
  public int count();
  
  @Select("DELETE FROM SCORES WHERE USERS_USER_ID = #{userId} AND SONGS_SONG_ID = #{songId}")
  public void delete(@Param("songId") String songId, @Param("userId") String userId);

  @Delete("DELETE FROM SCORES")
  public int deleteAll();
}

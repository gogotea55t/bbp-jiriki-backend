package io.github.gogotea55t.jiriki.domain.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import io.github.gogotea55t.jiriki.domain.Score4SongResponse;
import io.github.gogotea55t.jiriki.domain.entity.Scores;

@Mapper
public interface ScoresRepository {
  @SelectProvider(type = ScoreSqlBuilder.class, method = "buildScoreFetchSql")
  public Optional<Scores> findByUsers_UserIdAndSongs_SongId(String userId, String songId);

  @SelectProvider(type = ScoreSqlBuilder.class, method = "buildScoreFetchBySongIdSql")  
  public List<Score4SongResponse> findScoresBySongId(String songId);
  
  @Insert("<script>"
          + "INSERT INTO SCORES (USERS_USER_ID, SONGS_SONG_ID, SCORE) VALUES "
          + "<foreach item=\"songs\" collection=\"list\" separator=\",\"> "
          + "( #{userId}, #{songId}, #{score} )"
          + "</foreach>"
          + "</script>")
  public int saveAll(List<Scores> scores);

}

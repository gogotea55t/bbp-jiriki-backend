package io.github.gogotea55t.jiriki.domain.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

import io.github.gogotea55t.jiriki.domain.entity.Songs;
import io.github.gogotea55t.jiriki.domain.response.Score4UserResponse;
import io.github.gogotea55t.jiriki.domain.response.Score4UserResponseV2;
import io.github.gogotea55t.jiriki.domain.response.SongsResponse;

@Mapper
public interface SongRepository {
  @SelectProvider(type = SongsSqlBuilder.class, method = "buildSongSearchSql")
  public List<SongsResponse> searchSongsByConditions(
      Map<String, String> searchConditions, RowBounds rb);

  @SelectProvider(type = SongsSqlBuilder.class, method = "buildAverageScoreSearchSql")
  public List<Score4UserResponse> searchAverageByConditions(
      Map<String, String> searchConditions, RowBounds rb);

  @SelectProvider(type = SongsSqlBuilder.class, method = "buildAverageScoreSearchSqlV2")
  public List<Score4UserResponseV2> searchAverageByConditionsV2(
      Map<String, String> searchConditions, RowBounds rb);

  @SelectProvider(type = SongsSqlBuilder.class, method = "buildScoreSearchSql")
  public List<Score4UserResponse> searchScoreByConditions(
      Map<String, String> searchConditions, RowBounds rb);

  @SelectProvider(type = SongsSqlBuilder.class, method = "buildScoreSearchSqlV2")
  public List<Score4UserResponseV2> searchScoreByConditionsV2(
      Map<String, String> searchConditions, RowBounds rb);

  @Insert(
      "<script>"
          + "INSERT INTO SONGS (SONG_ID, JIRIKI_RANK, SONG_NAME, CONTRIBUTOR, INSTRUMENT) VALUES "
          + "<foreach item=\"songs\" collection=\"list\" separator=\",\"> "
          + "( #{songs.songId}, #{songs.jirikiRank}, #{songs.songName}, #{songs.contributor}, #{songs.instrument} )"
          + "</foreach>"
          + "</script>")
  public void saveAll(List<Songs> songs);

  @Select("SELECT * FROM SONGS")
  public List<Songs> findAll(RowBounds page);

  @Select("SELECT COUNT(*) FROM SONGS")
  public int count();

  @SelectProvider(type = SongsSqlBuilder.class, method = "countByCondition")
  public int countByCondition(Map<String, String> query);

  @SelectProvider(type = SongsSqlBuilder.class, method = "buildRandomSongSql")
  public Songs findSongByRandom(
      @Param("query") Map<String, String> query, @Param("randomOffset") int randomOffset);

  @Insert(
      "INSERT INTO SONGS (SONG_ID, JIRIKI_RANK, SONG_NAME, CONTRIBUTOR, INSTRUMENT) VALUES "
          + "( #{songId}, #{jirikiRank}, #{songName}, #{contributor}, #{instrument} )")
  public int save(Songs song);

  @Update(
      "UPDATE SONGS SET "
          + "JIRIKI_RANK = #{jirikiRank}, "
          + "SONG_NAME = #{songName}, "
          + "CONTRIBUTOR = #{contributor}, "
          + "INSTRUMENT = #{instrument} "
          + "WHERE SONG_ID = #{songId}")
  public int update(Songs song);

  @Delete("DELETE FROM SONGS")
  public void deleteAll();

  @Select("SELECT * FROM SONGS WHERE SONG_ID = #{id}")
  public Optional<Songs> findById(String id);
}

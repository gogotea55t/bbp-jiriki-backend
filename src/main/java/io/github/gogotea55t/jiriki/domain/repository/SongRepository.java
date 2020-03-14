package io.github.gogotea55t.jiriki.domain.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.session.RowBounds;

import io.github.gogotea55t.jiriki.domain.Score4UserResponse;
import io.github.gogotea55t.jiriki.domain.SongsResponse;
import io.github.gogotea55t.jiriki.domain.entity.Songs;

@Mapper
public interface SongRepository {
  @SelectProvider(type = SongsSqlBuilder.class, method = "buildSongSearchSql")
  public List<SongsResponse> searchSongsByConditions(
      Map<String, String> searchConditions, RowBounds rb);

  @SelectProvider(type = SongsSqlBuilder.class, method = "buildAverageScoreSearchSql")
  public List<Score4UserResponse> searchAverageByConditions(
      Map<String, String> searchConditions, RowBounds rb);

  @SelectProvider(type = SongsSqlBuilder.class, method = "buildScoreSearchSql")
  public List<Score4UserResponse> searchScoreByConditions(
      String userId, Map<String, String> searchConditions, RowBounds rb);

  @Insert(
      "<script>"
          + "INSERT INTO SONGS (SONG_ID, JIRIKI_RANK, SONG_NAME, CONTRIBUTOR, INSTRUMENT) VALUES "
          + "<foreach item=\"songs\" collection=\"list\" separator=\",\"> "
          + "( #{songId}, #{jirikiRank}, #{songName}, #{contributor}, #{instrument} )"
          + "</foreach>"
          + "</script>")
  public void saveAll(List<Songs> songs);

  @Select("SELECT * FROM SONGS")
  public List<Songs> findAll(RowBounds page);

  @Insert("INSERT INTO SONGS (SONG_ID, JIRIKI_RANK, SONG_NAME, CONTRIBUTOR, INSTRUMENT) VALUES "
		  + "( #{songId}, #{jirikiRank}, #{songName}, #{contributor}, #{instrument} )")
  public int save(Songs song);

  @Delete("DELETE FROM SONGS")
  public void deleteAll();

  @Select("SELECT * FROM SONGS WHERE SONG_ID = #{id}")
  public Optional<Songs> findById(String id);
}

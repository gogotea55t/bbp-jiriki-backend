package io.github.gogotea55t.jiriki.domain.repository;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.github.gogotea55t.jiriki.domain.Score4UserResponse;
import io.github.gogotea55t.jiriki.domain.entity.Songs;
import io.github.gogotea55t.jiriki.domain.vo.JirikiRank;

@Repository
public interface SongRepository extends JpaRepository<Songs, String> {
  public List<Songs> findBySongNameContaining(String songName, Pageable page);

  public List<Songs> findByContributorContaining(String contributor, Pageable page);

  public List<Songs> findByInstrumentContaining(String instrument, Pageable page);

  @Query(
      value =
          "SELECT so.song_id, so.jiriki_rank, so.song_name, so.contributor, so.instrument, sc.score "
              + "FROM songs so "
              + "LEFT OUTER JOIN scores sc "
              + "ON sc.songs_song_id = so.song_id "
              + "AND sc.users_user_id = ?1 "
              + "ORDER BY so.jiriki_rank asc",
      countQuery = "SELECT COUNT(*) FROM songs",
      nativeQuery = true)
  public List<Object[]> findSongsByUserIdWithEmptyRows(String userId, Pageable page);

  default List<Score4UserResponse> findSongsByUserIdWithEmpty(String userId, Pageable page) {
    return findSongsByUserIdWithEmptyRows(userId, page)
        .stream()
        .map(Score4UserResponse::new)
        .collect(Collectors.toList());
  }

  @Query(
      value =
          "SELECT so.song_id, so.jiriki_rank, so.song_name, so.contributor, so.instrument, sc.score "
              + "FROM songs so "
              + "LEFT OUTER JOIN scores sc "
              + "ON sc.songs_song_id = so.song_id "
              + "AND sc.users_user_id = :userid "
              + "WHERE so.song_name LIKE %:songname% "
              + "ORDER BY so.jiriki_rank asc",
      countQuery = "SELECT COUNT(*) FROM songs",
      nativeQuery = true)
  public List<Object[]> findSongsByUserIdAndSongNameWithEmptyRows(
      @Param("userid") String userId, @Param("songname") String songName, Pageable page);

  default List<Score4UserResponse> findSongsByUserIdAndSongNameWithEmpty(
      String userId, String songName, Pageable page) {
    return findSongsByUserIdAndSongNameWithEmptyRows(userId, songName, page)
        .stream()
        .map(Score4UserResponse::new)
        .collect(Collectors.toList());
  }

  @Query(
      value =
          "SELECT so.song_id, so.jiriki_rank, so.song_name, so.contributor, so.instrument, sc.score "
              + "FROM songs so "
              + "LEFT OUTER JOIN scores sc "
              + "ON sc.songs_song_id = so.song_id "
              + "AND sc.users_user_id = :userid "
              + "WHERE so.contributor LIKE %:contributor% "
              + "ORDER BY so.jiriki_rank asc",
      countQuery = "SELECT COUNT(*) FROM songs",
      nativeQuery = true)
  public List<Object[]> findSongsByUserIdAndContributorWithEmptyRows(
      @Param("userid") String userId, @Param("contributor") String contributor, Pageable page);

  default List<Score4UserResponse> findSongsByUserIdAndContributorWithEmpty(
      String userId, String contributor, Pageable page) {
    return findSongsByUserIdAndContributorWithEmptyRows(userId, contributor, page)
        .stream()
        .map(Score4UserResponse::new)
        .collect(Collectors.toList());
  }

  @Query(
      value =
          "SELECT so.song_id, so.jiriki_rank, so.song_name, so.contributor, so.instrument, sc.score "
              + "FROM songs so "
              + "LEFT OUTER JOIN scores sc "
              + "ON sc.songs_song_id = so.song_id "
              + "AND sc.users_user_id = :userid "
              + "WHERE so.instrument LIKE %:instrument% "
              + "ORDER BY so.jiriki_rank asc",
      countQuery = "SELECT COUNT(*) FROM songs",
      nativeQuery = true)
  public List<Object[]> findSongsByUserIdAndInstrumentWithEmptyRows(
      @Param("userid") String userId, @Param("instrument") String instrument, Pageable page);

  default List<Score4UserResponse> findSongsByUserIdAndInstrumentWithEmpty(
      String userId, String instrument, Pageable page) {
    return findSongsByUserIdAndInstrumentWithEmptyRows(userId, instrument, page)
        .stream()
        .map(Score4UserResponse::new)
        .collect(Collectors.toList());
  }

  @Query(
      value =
          "SELECT so.song_id, so.jiriki_rank, so.song_name, so.contributor, so.instrument, sc.score "
              + "FROM songs so "
              + "LEFT OUTER JOIN scores sc "
              + "ON sc.songs_song_id = so.song_id "
              + "AND sc.users_user_id = :userid "
              + "WHERE so.jiriki_rank = :jiriki "
              + "ORDER BY so.jiriki_rank asc",
      countQuery = "SELECT COUNT(*) FROM songs",
      nativeQuery = true)
  public List<Object[]> findSongsByUserIdAndJirikiWithEmptyRows(
      @Param("userid") String userId, @Param("jiriki") int jiriki, Pageable page);

  default List<Score4UserResponse> findSongsByUserIdAndJirikiRankWithEmpty(
      String userId, JirikiRank jiriki, Pageable page) {
    return findSongsByUserIdAndJirikiWithEmptyRows(userId, jiriki.getJirikiId(), page)
        .stream()
        .map(Score4UserResponse::new)
        .collect(Collectors.toList());
  }
}

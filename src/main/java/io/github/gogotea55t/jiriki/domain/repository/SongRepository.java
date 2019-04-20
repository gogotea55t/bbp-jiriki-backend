package io.github.gogotea55t.jiriki.domain.repository;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.github.gogotea55t.jiriki.domain.Score4UserResponse;
import io.github.gogotea55t.jiriki.domain.entity.Songs;

@Repository
public interface SongRepository extends JpaRepository<Songs, String> {
  public List<Songs> findBySongNameContaining(String songName, Pageable page);

  public List<Songs> findByContributorContaining(String contributor, Pageable page);

  public List<Songs> findByInstrumentContaining(String instrument, Pageable page);

  @Query(
      value =
          "SELECT so.song_id, so.song_name, so.contributor, so.instrument, sc.score "
              + "FROM songs so "
              + "LEFT OUTER JOIN scores sc "
              + "ON sc.songs_song_id = so.song_id "
              + "AND sc.users_user_id = ?1 "
              + "ORDER BY so.song_id asc",
      countQuery = "SELECT COUNT(*) FROM songs",
      nativeQuery = true)
  public List<Object[]> findSongsByUserIdWithEmptyRows(String userId, Pageable page);

  default List<Score4UserResponse> findSongsByUserIdWithEmpty(String userId, Pageable page) {
    return findSongsByUserIdWithEmptyRows(userId, page)
        .stream()
        .map(Score4UserResponse::new)
        .collect(Collectors.toList());
  }
}

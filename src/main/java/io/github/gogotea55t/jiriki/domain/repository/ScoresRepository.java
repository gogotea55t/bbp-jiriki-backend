package io.github.gogotea55t.jiriki.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.gogotea55t.jiriki.domain.entity.Scores;

@Repository
public interface ScoresRepository extends JpaRepository<Scores, Long> {
  Optional<Scores> findByUsers_UserIdAndSongs_SongId(String userId, String songId);
}

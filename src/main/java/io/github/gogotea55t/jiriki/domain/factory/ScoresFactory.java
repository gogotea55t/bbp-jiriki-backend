package io.github.gogotea55t.jiriki.domain.factory;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.gogotea55t.jiriki.domain.entity.Scores;
import io.github.gogotea55t.jiriki.domain.entity.Songs;
import io.github.gogotea55t.jiriki.domain.entity.Users;
import io.github.gogotea55t.jiriki.domain.repository.ScoresRepository;
import io.github.gogotea55t.jiriki.domain.repository.SongRepository;
import io.github.gogotea55t.jiriki.domain.repository.UserRepository;
import io.github.gogotea55t.jiriki.domain.request.ScoreRequest;

@Component
public class ScoresFactory {
  private ScoresRepository scoreRepository;
  private SongRepository songRepository;
  private UserRepository userRepository;

  @Autowired
  public ScoresFactory(
      SongRepository songRepository,
      UserRepository userRepository,
      ScoresRepository scoreRepository) {
    this.scoreRepository = scoreRepository;
    this.songRepository = songRepository;
    this.userRepository = userRepository;
  }

  public Scores generateScoreFrom(ScoreRequest request) {
    Scores score = new Scores();
    Optional<Users> user = userRepository.findById(request.getUserId());
    if (!user.isPresent()) {
      throw new IllegalArgumentException("ユーザーの情報がありません。");
    }
    score.setUsers(user.get());
    Optional<Songs> song = songRepository.findById(request.getSongId());
    if (!song.isPresent()) {
      throw new IllegalArgumentException("楽曲の情報がありません。");
    }
    score.setSongs(song.get());
    if (!request.getScore().isInsertableToDB()) {
      throw new IllegalArgumentException("整数のみ指定可能です。");
    }
    score.setScore(request.getScore());
    return score;
  }
}

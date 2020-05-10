package io.github.gogotea55t.jiriki.domain;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.gogotea55t.jiriki.domain.entity.Songs;
import io.github.gogotea55t.jiriki.domain.repository.ScoresRepository;
import io.github.gogotea55t.jiriki.domain.repository.SongRepository;
import io.github.gogotea55t.jiriki.domain.repository.UserRepository;
import io.github.gogotea55t.jiriki.domain.request.PageRequest;
import io.github.gogotea55t.jiriki.domain.response.Score4SongResponse;
import io.github.gogotea55t.jiriki.domain.response.Score4SongResponseV2;
import io.github.gogotea55t.jiriki.domain.response.Score4UserResponse;
import io.github.gogotea55t.jiriki.domain.response.Score4UserResponseV2;
import io.github.gogotea55t.jiriki.domain.response.SongsResponse;

@Service
public class SongService {
  private SongRepository songRepository;
  private UserRepository userRepository;
  private ScoresRepository scoreRepository;

  @Autowired
  public SongService(SongRepository songRepository, UserRepository userRepository, ScoresRepository scoreRepository) {
    this.songRepository = songRepository;
    this.userRepository = userRepository;
    this.scoreRepository = scoreRepository;
  }

  public SongsResponse getSongBySongId(String songId) {
    Optional<Songs> response = songRepository.findById(songId);
    if (response.isPresent()) {
      return SongsResponse.of(response.get());
    } else {
      return null;
    }
  }

  public SongsResponse getSongByRandom() {

    return SongsResponse.of(getSongEntityByRandom());
  }

  public Songs getSongEntityByRandom() {
    int numberOfSongs = songRepository.count();
    Random random = new Random();
    return songRepository.findSongByRandom(random.nextInt(numberOfSongs));
  }

  public List<SongsResponse> searchSongsByQuery(Map<String, String> query, PageRequest page) {
    return songRepository.searchSongsByConditions(query, page.getRb());
  }

  public List<Score4UserResponse> searchAverageScoresByQuery(
      Map<String, String> query, PageRequest page) {
    return songRepository.searchAverageByConditions(query, page.getRb());
  }

  public List<Score4UserResponse> searchScoresByQuery(
      String userId, Map<String, String> query, PageRequest page) {
    if (userRepository.findById(userId).isPresent()) {
      query.put("userId", userId);
      return songRepository.searchScoreByConditions(query, page.getRb());
    } else {
      throw new IllegalArgumentException("User not found.");
    }
  }

  public List<Score4UserResponseV2> searchScoresByQueryV2(
      String userId, Map<String, String> query, PageRequest page) {
    if (userRepository.findById(userId).isPresent()) {
      query.put("userId", userId);
      return songRepository.searchScoreByConditionsV2(query, page.getRb());
    } else {
      throw new IllegalArgumentException("User not found.");
    }
  }

  public List<Score4SongResponse> getScoresBySongId(String songId) {
    Optional<Songs> song = songRepository.findById(songId);
    if (song.isPresent()) {
      return scoreRepository.findScoresBySongId(songId);
    } else {
      return null;
    }
  }

  public List<Score4SongResponseV2> getScoresBySongIdV2(String songId) {
    Optional<Songs> song = songRepository.findById(songId);
    if (song.isPresent()) {
      return scoreRepository.findScoresBySongIdV2(songId);
    } else {
      return null;
    }
  }
}

package io.github.gogotea55t.jiriki.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.gogotea55t.jiriki.domain.entity.Songs;
import io.github.gogotea55t.jiriki.domain.exception.SongsNotFoundException;
import io.github.gogotea55t.jiriki.domain.repository.ScoresRepository;
import io.github.gogotea55t.jiriki.domain.repository.SongRepository;
import io.github.gogotea55t.jiriki.domain.repository.UserRepository;
import io.github.gogotea55t.jiriki.domain.request.PageRequest;
import io.github.gogotea55t.jiriki.domain.response.Score4SongResponse;
import io.github.gogotea55t.jiriki.domain.response.Score4SongResponseV2;
import io.github.gogotea55t.jiriki.domain.response.Score4UserResponse;
import io.github.gogotea55t.jiriki.domain.response.Score4UserResponseV2;
import io.github.gogotea55t.jiriki.domain.response.SongTopScoreResponse;
import io.github.gogotea55t.jiriki.domain.response.SongsResponse;
import io.github.gogotea55t.jiriki.domain.response.StatisticResponse;
import io.github.gogotea55t.jiriki.domain.vo.song.SongId;
import io.github.gogotea55t.jiriki.domain.vo.user.UserId;

@Service
public class SongService {
  private SongRepository songRepository;
  private UserRepository userRepository;
  private ScoresRepository scoreRepository;

  @Autowired
  public SongService(
      SongRepository songRepository,
      UserRepository userRepository,
      ScoresRepository scoreRepository) {
    this.songRepository = songRepository;
    this.userRepository = userRepository;
    this.scoreRepository = scoreRepository;
  }

  public SongsResponse getSongBySongId(SongId songId) {
    Optional<Songs> response = songRepository.findById(songId);
    if (response.isPresent()) {
      return SongsResponse.of(response.get());
    } else {
      return null;
    }
  }

  public SongsResponse getSongByRandom(Map<String, String> query) {
    Songs song = getSongEntityByRandom(query);
    return SongsResponse.of(song);
  }

  public Songs getSongEntityByRandom(Map<String, String> query) {
    int numberOfSongs = songRepository.countByCondition(query);
    if (numberOfSongs == 0) {
      throw new SongsNotFoundException("指定された条件に該当する曲がありません。");
    }
    Random random = new Random();
    int rand = random.nextInt(numberOfSongs);
    return songRepository.findSongByRandom(query, rand);
  }

  public List<SongsResponse> searchSongsByQuery(Map<String, String> query, PageRequest page) {
    return songRepository.searchSongsByConditions(query, page.getRb());
  }

  public List<Score4UserResponse> searchAverageScoresByQuery(
      Map<String, String> query, PageRequest page) {
    return songRepository.searchAverageByConditions(query, page.getRb());
  }
  
  public List<Score4UserResponseV2> searchAverageScoresByQueryV2(
	      Map<String, String> query, PageRequest page) {
	    return songRepository.searchAverageByConditionsV2(query, page.getRb());
	  }

  public List<Score4UserResponse> searchScoresByQuery(
      UserId userId, Map<String, String> query, PageRequest page) {
    if (userRepository.findById(userId).isPresent()) {
      query.put("userId", userId.getValue());
      return songRepository.searchScoreByConditions(query, page.getRb());
    } else {
      throw new IllegalArgumentException("User not found.");
    }
  }

  public List<Score4UserResponseV2> searchScoresByQueryV2(
      UserId userId, Map<String, String> query, PageRequest page) {
    if (userRepository.findById(userId).isPresent()) {
      query.put("userId", userId.getValue());
      return songRepository.searchScoreByConditionsV2(query, page.getRb());
    } else {
      throw new IllegalArgumentException("User not found.");
    }
  }

  public List<Score4SongResponse> getScoresBySongId(SongId songId) {
    Optional<Songs> song = songRepository.findById(songId);
    if (song.isPresent()) {
      return scoreRepository.findScoresBySongId(songId);
    } else {
      return null;
    }
  }

  public List<Score4SongResponseV2> getScoresBySongIdV2(SongId songId) {
    Optional<Songs> song = songRepository.findById(songId);
    if (song.isPresent()) {
      return scoreRepository.findScoresBySongIdV2(songId);
    } else {
      return null;
    }
  }

  public SongTopScoreResponse getSongTopScore(SongId songId) {
    List<Score4SongResponseV2> results = scoreRepository.findScoresBySongIdV2(songId);
    List<Score4SongResponseV2> top = new ArrayList<>();
    List<Score4SongResponseV2> second = new ArrayList<>();
    List<Score4SongResponseV2> third = new ArrayList<>();
    int topScore = 0;
    int secondScore = 0;
    int thirdScore = 0;
    for (Score4SongResponseV2 result : results) {
      if (result.getScore().largerThan(topScore)) {
        topScore = result.getScore().getScore().intValue();
        top.add(result);
      } else if (result.getScore().largerThan(secondScore)) {
        secondScore = result.getScore().getScore().intValue();
        second.add(result);
      } else if (result.getScore().largerThan(thirdScore)) {
        thirdScore = result.getScore().getScore().intValue();
        third.add(result);
      } else {
        // スコア順に並んでいる想定なのでここまで来たらもう4位以下と考えて処理を打ち切る
        break;
      }
    }

    SongTopScoreResponse response = new SongTopScoreResponse();
    response.setTop(top);
    response.setSecond(second);
    response.setThird(third);
    return response;
  }
  
  public StatisticResponse getSongStat(SongId songId) {
	return scoreRepository.getStatisticsOfSongs(songId);
  }
}

package io.github.gogotea55t.jiriki.domain;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.gogotea55t.jiriki.AuthService;
import io.github.gogotea55t.jiriki.domain.entity.Scores;
import io.github.gogotea55t.jiriki.domain.factory.ScoresFactory;
import io.github.gogotea55t.jiriki.domain.repository.ScoresRepository;
import io.github.gogotea55t.jiriki.domain.request.ScoreDeleteRequest;
import io.github.gogotea55t.jiriki.domain.request.ScoreRequest;
import io.github.gogotea55t.jiriki.domain.response.StatisticResponse;
import io.github.gogotea55t.jiriki.domain.response.StatisticsResponseByJirikiRank;

@Service
public class ScoreService {
  private ScoresRepository scoreRepository;
  private AuthService authService;
  private ScoresFactory scoreFactory;

  public ScoreService(AuthService authService, ScoresRepository scoreRepository, ScoresFactory scoreFactory) {
    this.authService = authService;
    this.scoreRepository = scoreRepository;
    this.scoreFactory = scoreFactory;
  }
  
  /**
   * スコアを登録してDBに格納する
   * @param request
   */
  public void registerScore(ScoreRequest request) {
    Scores score = scoreFactory.generateScoreFrom(request);
    if (scoreRepository
        .findByUsers_UserIdAndSongs_SongId(request.getUserId(), request.getSongId())
        .isPresent()) {
      score.setUpdatedBy(getUserSubjectFromToken());
      scoreRepository.update(score);
    } else {
      score.setCreatedBy(getUserSubjectFromToken());
      score.setUpdatedBy(getUserSubjectFromToken());
      scoreRepository.save(score);
    }
  }

  /**
   * ScoreをDBから削除する
   * @param request
   * @return 削除したら1, 削除できなければ0
   */
  public int deleteScore(ScoreDeleteRequest request) {
    if (scoreRepository
        .findByUsers_UserIdAndSongs_SongId(request.getUserId(), request.getSongId())
        .isPresent()) {
      scoreRepository.delete(request.getSongId(), request.getUserId());
      return 1;
    } else {
      return 0;
    }
  }
  
  public StatisticResponse getScoreStatisticsByUserId(String userId) {
	return scoreRepository.getStatisticsOfUsers(userId, false).get(0).getStats();
  }
  
  public List<StatisticsResponseByJirikiRank> getScoreStatisticsByUserIdGroupByJirikiRank(String userId) {
	return scoreRepository.getStatisticsOfUsers(userId, true);  
  }

  public String getUserSubjectFromToken() {
    return authService.getUserSubjectFromToken();
  }
}

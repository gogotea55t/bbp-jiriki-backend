package io.github.gogotea55t.jiriki.domain.vo;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.gogotea55t.jiriki.domain.SampleDatum;
import io.github.gogotea55t.jiriki.domain.entity.Scores;
import io.github.gogotea55t.jiriki.domain.repository.ScoresRepository;
import io.github.gogotea55t.jiriki.domain.repository.SongRepository;
import io.github.gogotea55t.jiriki.domain.repository.UserRepository;
import io.micrometer.core.instrument.LongTaskTimer.Sample;

@RunWith(SpringRunner.class)
@ActiveProfiles("unittest")
@SpringBootTest
public class ScoreValueTestWithDB {

  @Autowired private UserRepository userRepository;

  @Autowired private SongRepository songRepository;

  @Autowired private ScoresRepository scoreRepository;

  private SampleDatum sample = new SampleDatum();

  @Before
  public void init() {
    userRepository.deleteAll();
    songRepository.deleteAll();
    scoreRepository.deleteAll();

    userRepository.saveAll(sample.getUsers());
    songRepository.saveAll(sample.getSongs());
    scoreRepository.saveAll(sample.getScores());
  }

  @Test
  public void 新規登録できる() {
    Scores score = new Scores();
    score.setScoreId(6);
    score.setSongs(sample.getSongs().get(0));
    score.setUsers(sample.getUsers().get(0));

    Integer test = 30;
    score.setScore(new ScoreValue(test));
    scoreRepository.save(score);
  }
  
  @Test(expected = InvalidDataAccessApiUsageException.class)
  public void 小数点以下があると登録できない() {
    Scores score = new Scores();
    score.setScoreId(6);
    score.setSongs(sample.getSongs().get(0));
    score.setUsers(sample.getUsers().get(0));

    BigDecimal test = BigDecimal.valueOf(23.45);
    score.setScore(new ScoreValue(test));
    scoreRepository.save(score);
  }
  
  @Test(expected = InvalidDataAccessApiUsageException.class)
  public void 小数点以下があるとキリが良くても登録できない() {
    Scores score = new Scores();
    score.setScoreId(6);
    score.setSongs(sample.getSongs().get(0));
    score.setUsers(sample.getUsers().get(0));

    BigDecimal test = BigDecimal.valueOf(23.00);
    score.setScore(new ScoreValue(test));
    scoreRepository.save(score);
  }

  @Test(expected = InvalidDataAccessApiUsageException.class)
  public void スコアがnullだとDBに登録できない() throws Exception {
    Scores score = new Scores();
    score.setScoreId(6);
    score.setSongs(sample.getSongs().get(0));
    score.setUsers(sample.getUsers().get(0));

    BigDecimal test = null;
    score.setScore(new ScoreValue(test));
    scoreRepository.save(score);
  }
}

package io.github.gogotea55t.jiriki.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.gogotea55t.jiriki.domain.SampleDatum;
import io.github.gogotea55t.jiriki.domain.entity.Scores;
import io.github.gogotea55t.jiriki.domain.repository.ScoresRepository;
import io.github.gogotea55t.jiriki.domain.repository.SongRepository;
import io.github.gogotea55t.jiriki.domain.repository.UserRepository;

@RunWith(SpringRunner.class)
@ActiveProfiles("unittest")
@SpringBootTest
public class ScoreValueTestWithDB {
  @Rule public ExpectedException expectedException = ExpectedException.none();

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
    score.setSongs(sample.getSongs().get(1));
    score.setUsers(sample.getUsers().get(1));

    Integer test = 30;
    score.setScore(new ScoreValue(test));
    scoreRepository.save(score);
  }

  @Test
  public void 小数点以下があると登録できない() {
    expectedException.expect(MyBatisSystemException.class);
    Scores score = new Scores();
    score.setScoreId(6);
    score.setSongs(sample.getSongs().get(1));
    score.setUsers(sample.getUsers().get(1));

    BigDecimal test = BigDecimal.valueOf(23.45);
    score.setScore(new ScoreValue(test));

    scoreRepository.save(score);
  }

  @Test
  public void 小数点以下があってもキリが良ければ登録OK() {

    Scores score = new Scores();
    score.setScoreId(6);
    score.setSongs(sample.getSongs().get(1));
    score.setUsers(sample.getUsers().get(1));

    BigDecimal test = BigDecimal.valueOf(23.00);
    score.setScore(new ScoreValue(test));
    scoreRepository.save(score);
    assertThat(
            scoreRepository
                .findByUsers_UserIdAndSongs_SongId(
                    sample.getUsers().get(1).getUserId(), sample.getSongs().get(1).getSongId())
                .isPresent())
        .isTrue();
  }

  @Test
  public void スコアがnullだとDBに登録できない() throws Exception {
    expectedException.expect(MyBatisSystemException.class);

    Scores score = new Scores();
    score.setScoreId(6);
    score.setSongs(sample.getSongs().get(1));
    score.setUsers(sample.getUsers().get(1));

    BigDecimal test = null;
    score.setScore(new ScoreValue(test));
    scoreRepository.save(score);
  }
}

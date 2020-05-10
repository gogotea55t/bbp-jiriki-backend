package io.github.gogotea55t.jiriki.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.gogotea55t.jiriki.AuthService;
import io.github.gogotea55t.jiriki.domain.entity.Scores;
import io.github.gogotea55t.jiriki.domain.factory.ScoresFactory;
import io.github.gogotea55t.jiriki.domain.repository.ScoresRepository;
import io.github.gogotea55t.jiriki.domain.repository.SongRepository;
import io.github.gogotea55t.jiriki.domain.repository.UserRepository;
import io.github.gogotea55t.jiriki.domain.request.ScoreDeleteRequest;
import io.github.gogotea55t.jiriki.domain.request.ScoreRequest;
import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;

@RunWith(SpringRunner.class)
@ActiveProfiles("unittest")
@SpringBootTest
public class ScoreServiceTest {
  @Autowired private ScoresRepository scoreRepository;
  @MockBean private AuthService mockAuthService;
  @Autowired private ScoresFactory scoreFactory;
  @Autowired private UserRepository userRepository;
  @Autowired private SongRepository songRepository;
  
  @Rule public ExpectedException expectedException = ExpectedException.none();

  private ScoreService scoreService;

  @Before
  public void init() {
    SampleDatum sample = new SampleDatum();
    userRepository.deleteAll();
    songRepository.deleteAll();
    scoreRepository.deleteAll();
    userRepository.saveAll(sample.getUsers());
    songRepository.saveAll(sample.getSongs());
    scoreRepository.saveAll(sample.getScores());
    
    scoreService = new ScoreService(mockAuthService, scoreRepository, scoreFactory);
  }

  @Test
  public void スコアの登録ができる() throws Exception {
    ScoreRequest request = new ScoreRequest();
    request.setSongId("002");
    request.setUserId("u002");
    request.setScore(new ScoreValue(38));
    when(mockAuthService.getUserSubjectFromToken()).thenReturn("tw|00022323");

    scoreService.registerScore(request);
    Optional<Scores> score = scoreRepository.findByUsers_UserIdAndSongs_SongId("u002", "002");
    assertThat(score.isPresent()).isTrue();
    assertThat(score.get().getScore()).isEqualTo(new ScoreValue(new BigDecimal(38)));
    score.ifPresent(
        sc -> {
          assertThat(sc.getUpdatedBy()).isEqualTo("tw|00022323");
        });
  }

  @Test
  public void スコアの更新ができる() throws Exception {
    ScoreRequest request = new ScoreRequest();
    request.setSongId("001");
    request.setUserId("u001");
    request.setScore(new ScoreValue(90));
    when(mockAuthService.getUserSubjectFromToken()).thenReturn("tw|00022323");

    scoreService.registerScore(request);
    Optional<Scores> score = scoreRepository.findByUsers_UserIdAndSongs_SongId("u001", "001");
    assertThat(score.isPresent()).isTrue();
    assertThat(score.get().getScore()).isEqualTo(new ScoreValue(new BigDecimal(90)));
    score.ifPresent(
        sc -> {
          assertThat(sc.getCreatedBy()).isEqualTo("ANONYMOUS");
        });
    score.ifPresent(
        sc -> {
          assertThat(sc.getUpdatedBy()).isEqualTo("tw|00022323");
        });
  }

  @Test
  public void 存在しない楽曲のスコアの更新はできない() throws Exception {
    expectedException.expect(IllegalArgumentException.class);
    ScoreRequest request = new ScoreRequest();
    request.setSongId("901");
    request.setUserId("u001");
    request.setScore(new ScoreValue(90));
    when(mockAuthService.getUserSubjectFromToken()).thenReturn("tw|00022323");
    scoreService.registerScore(request);
  }

  @Test
  public void 存在しないユーザのスコアの更新はできない() throws Exception {
    expectedException.expect(IllegalArgumentException.class);
    ScoreRequest request = new ScoreRequest();
    request.setSongId("001");
    request.setUserId("u901");
    request.setScore(new ScoreValue(90));
    when(mockAuthService.getUserSubjectFromToken()).thenReturn("tw|00022323");
    scoreService.registerScore(request);
  }

  @Test
  public void 小数点以下のスコアの更新はできない() throws Exception {
    expectedException.expect(IllegalArgumentException.class);
    ScoreRequest request = new ScoreRequest();
    request.setSongId("001");
    request.setUserId("u001");
    request.setScore(new ScoreValue(90.66));
    when(mockAuthService.getUserSubjectFromToken()).thenReturn("tw|00022323");
    scoreService.registerScore(request);
  }

  @Test
  public void スコアの削除ができる() throws Exception {
    ScoreDeleteRequest request = new ScoreDeleteRequest();
    request.setSongId("001");
    request.setUserId("u001");

    assertThat(scoreService.deleteScore(request)).isEqualTo(1);
    assertThat(scoreRepository.findByUsers_UserIdAndSongs_SongId("u001", "001").isPresent())
        .isFalse();
  }

  @Test
  public void 存在しないスコアの削除はしない() throws Exception {
    ScoreDeleteRequest request = new ScoreDeleteRequest();
    request.setUserId("u009");
    request.setSongId("004");

    assertThat(scoreService.deleteScore(request)).isEqualTo(0);
  }
}

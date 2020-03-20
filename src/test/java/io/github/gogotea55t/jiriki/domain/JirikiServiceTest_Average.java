package io.github.gogotea55t.jiriki.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import io.github.gogotea55t.jiriki.domain.entity.Songs;
import io.github.gogotea55t.jiriki.domain.entity.TwitterUsers;
import io.github.gogotea55t.jiriki.domain.factory.ScoresFactory;
import io.github.gogotea55t.jiriki.domain.repository.ScoresRepository;
import io.github.gogotea55t.jiriki.domain.repository.SongRepository;
import io.github.gogotea55t.jiriki.domain.repository.TwitterUsersRepository;
import io.github.gogotea55t.jiriki.domain.repository.UserRepository;
import io.github.gogotea55t.jiriki.domain.request.PageRequest;
import io.github.gogotea55t.jiriki.domain.vo.JirikiRank;
import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;

@RunWith(SpringRunner.class)
@ActiveProfiles("unittest")
@SpringBootTest
public class JirikiServiceTest_Average {
  @Autowired private GoogleSpreadSheetConfig sheetConfig;

  @Autowired private GoogleSheetsService sheetService;

  @Autowired private UserRepository userRepository;

  @Autowired private SongRepository songRepository;

  @Autowired private ScoresRepository scoreRepository;

  @Autowired private TwitterUsersRepository twiRepository;

  @Autowired private ScoresFactory scoreFactory;

  private JirikiService jirikiService;

  PageRequest defaultPaging = new PageRequest(0, 20);

  Map<String, String> query;

  @Transactional
  @Before
  public void init() {
    jirikiService =
        new JirikiService(
            sheetConfig,
            sheetService,
            userRepository,
            songRepository,
            scoreRepository,
            twiRepository,
            scoreFactory);
    SampleDatum sample = new SampleDatum();
    userRepository.deleteAll();
    songRepository.deleteAll();
    scoreRepository.deleteAll();
    twiRepository.deleteAll();

    userRepository.saveAll(sample.getUsers());
    songRepository.saveAll(sample.getSongs());
    scoreRepository.saveAll(sample.getScores());
    query = new HashMap<>();
    twiRepository.save(new TwitterUsers("twitterId", sample.getUsers().get(1)));
  }

  @Test
  public void 平均点を計算できる() throws Exception {
    List<Score4UserResponse> scores =
        jirikiService.searchAverageScoresByQuery(query, defaultPaging);
    assertThat(scores.size()).isEqualTo(2);
    // ↓なぜか失敗する
    // assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(new BigDecimal("96.50")));
    assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(96.50));
  }

  @Test
  public void 地力を指定して平均点を計算できる() throws Exception {
    query.put("jiriki", "地力Ｓ＋");
    List<Score4UserResponse> scores =
        jirikiService.searchAverageScoresByQuery(query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
    // ↓なぜか失敗する
    // assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(new BigDecimal("96.50")));
    assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(96.50));
  }

  @Test
  public void 曲名を指定して平均点を計算できる() throws Exception {
    query.put("name", "ミラクルペイント");
    List<Score4UserResponse> scores =
        jirikiService.searchAverageScoresByQuery(query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
    // ↓なぜか失敗する
    assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(new BigDecimal("99.00")));
    // assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(99));
  }

  @Test
  public void 投稿者名を指定して平均点を計算できる() throws Exception {
    query.put("contributor", "タタナミ");
    List<Score4UserResponse> scores =
        jirikiService.searchAverageScoresByQuery(query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
    // ↓なぜか失敗する
    assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(new BigDecimal("99.00")));
    // assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(99));
  }

  @Test
  public void 楽器名を指定して平均点を計算できる() throws Exception {
    query.put("instrument", "ピアノ");
    List<Score4UserResponse> scores =
        jirikiService.searchAverageScoresByQuery(query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
    // ↓なぜか失敗する
    assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(new BigDecimal("99.00")));
    // assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(96));
  }
}

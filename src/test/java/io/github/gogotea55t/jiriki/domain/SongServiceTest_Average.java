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

import io.github.gogotea55t.jiriki.domain.repository.ScoresRepository;
import io.github.gogotea55t.jiriki.domain.repository.SongRepository;
import io.github.gogotea55t.jiriki.domain.repository.UserRepository;
import io.github.gogotea55t.jiriki.domain.request.PageRequest;
import io.github.gogotea55t.jiriki.domain.response.Score4UserResponse;
import io.github.gogotea55t.jiriki.domain.response.Score4UserResponseV2;
import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;

@RunWith(SpringRunner.class)
@ActiveProfiles("unittest")
@SpringBootTest
public class SongServiceTest_Average {

  PageRequest defaultPaging = new PageRequest(0, 20);

  Map<String, String> query;

  private SongService songService;

  @Autowired private SongRepository songRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private ScoresRepository scoreRepository;

  SampleDatum sample = new SampleDatum();

  @Before
  public void init() {
    songService = new SongService(songRepository, userRepository, scoreRepository);
    songRepository.deleteAll();
    userRepository.deleteAll();
    scoreRepository.deleteAll();

    songRepository.saveAll(sample.getSongs());
    userRepository.saveAll(sample.getUsers());
    scoreRepository.saveAll(sample.getScores());

    query = new HashMap<>();
  }

  @Test
  public void 平均点を計算できる() throws Exception {
    List<Score4UserResponse> scores = songService.searchAverageScoresByQuery(query, defaultPaging);
    assertThat(scores.size()).isEqualTo(3);
    // ↓なぜか失敗する
    // assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(new BigDecimal("96.50")));
    assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(96.50));
  }

  @Test
  public void 地力を指定して平均点を計算できる() throws Exception {
    query.put("jiriki", "地力Ｓ＋");
    List<Score4UserResponse> scores = songService.searchAverageScoresByQuery(query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
    // ↓なぜか失敗する
    // assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(new BigDecimal("96.50")));
    assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(96.50));
  }

  @Test
  public void 曲名を指定して平均点を計算できる() throws Exception {
    query.put("name", "ミラクルペイント");
    List<Score4UserResponse> scores = songService.searchAverageScoresByQuery(query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
    // ↓なぜか失敗する
    assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(new BigDecimal("99.00")));
    // assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(99));
  }

  @Test
  public void 投稿者名を指定して平均点を計算できる() throws Exception {
    query.put("contributor", "タタナミ");
    List<Score4UserResponse> scores = songService.searchAverageScoresByQuery(query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
    // ↓なぜか失敗する
    assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(new BigDecimal("99.00")));
    // assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(99));
  }

  @Test
  public void 楽器名を指定して平均点を計算できる() throws Exception {
    query.put("instrument", "ピアノ");
    List<Score4UserResponse> scores = songService.searchAverageScoresByQuery(query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
    // ↓なぜか失敗する
    assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(new BigDecimal("99.00")));
    // assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(96));
  }
  
  @Test
  public void 平均点を計算できるV2() throws Exception {
    List<Score4UserResponseV2> scores = songService.searchAverageScoresByQueryV2(query, defaultPaging);
    assertThat(scores.size()).isEqualTo(3);
    // ↓なぜか失敗する
    // assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(new BigDecimal("96.50")));
    assertThat(scores.get(0).getAverage()).isEqualTo(new ScoreValue(96.50));
    assertThat(scores.get(0).getScore()).isNull();
  }

  @Test
  public void 地力を指定して平均点を計算できるV2() throws Exception {
    query.put("jiriki", "地力Ｓ＋");
    List<Score4UserResponseV2> scores = songService.searchAverageScoresByQueryV2(query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
    // ↓なぜか失敗する
    // assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(new BigDecimal("96.50")));
    assertThat(scores.get(0).getAverage()).isEqualTo(new ScoreValue(96.50));
    assertThat(scores.get(0).getScore()).isNull();
  }

  @Test
  public void 曲名を指定して平均点を計算できるV2() throws Exception {
    query.put("name", "ミラクルペイント");
    List<Score4UserResponseV2> scores = songService.searchAverageScoresByQueryV2(query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
    // ↓なぜか失敗する
    assertThat(scores.get(0).getAverage()).isEqualTo(new ScoreValue(new BigDecimal("99.00")));
    // assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(99));
    assertThat(scores.get(0).getScore()).isNull();
  }

  @Test
  public void 投稿者名を指定して平均点を計算できるV2() throws Exception {
    query.put("contributor", "タタナミ");
    List<Score4UserResponseV2> scores = songService.searchAverageScoresByQueryV2(query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
    // ↓なぜか失敗する
    assertThat(scores.get(0).getAverage()).isEqualTo(new ScoreValue(new BigDecimal("99.00")));
    // assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(99));
    assertThat(scores.get(0).getScore()).isNull();
  }

  @Test
  public void 楽器名を指定して平均点を計算できるV2() throws Exception {
    query.put("instrument", "ピアノ");
    List<Score4UserResponseV2> scores = songService.searchAverageScoresByQueryV2(query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
    // ↓なぜか失敗する
    assertThat(scores.get(0).getAverage()).isEqualTo(new ScoreValue(new BigDecimal("99.00")));
    // assertThat(scores.get(0).getScore()).isEqualTo(new ScoreValue(96));
    assertThat(scores.get(0).getScore()).isNull();
  }
}

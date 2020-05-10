package io.github.gogotea55t.jiriki.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.api.services.sheets.v4.model.ValueRange;

import io.github.gogotea55t.jiriki.AuthService;
import io.github.gogotea55t.jiriki.domain.entity.Scores;
import io.github.gogotea55t.jiriki.domain.entity.Songs;
import io.github.gogotea55t.jiriki.domain.entity.Users;
import io.github.gogotea55t.jiriki.domain.repository.ScoresRepository;
import io.github.gogotea55t.jiriki.domain.repository.SongRepository;
import io.github.gogotea55t.jiriki.domain.repository.UserRepository;
import io.github.gogotea55t.jiriki.domain.vo.JirikiRank;
import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;

@RunWith(SpringRunner.class)
@ActiveProfiles("unittest")
@SpringBootTest
public class JirikiServiceTestWithMock {
  @MockBean GoogleSheetsService sheetsService;
  @Autowired private UserRepository userRepository;

  @Autowired private SongRepository songRepository;

  @Autowired private ScoresRepository scoreRepository;
  
  @MockBean private AuthService authService;

  private JirikiService jirikiService;

  @Before
  public void init() {
    userRepository.deleteAll();
    songRepository.deleteAll();
    scoreRepository.deleteAll();
    jirikiService =
        new JirikiService(
            sheetsService,
            userRepository,
            songRepository,
            scoreRepository,
            authService);
    Users sampleUser = new Users();
    sampleUser.setUserId("u001");
    sampleUser.setUserName("妖怪1");
    userRepository.save(sampleUser);
    Users sampleUser2 = new Users();
    sampleUser2.setUserId("u002");
    sampleUser2.setUserName("妖怪b");
    userRepository.save(sampleUser2);

    Songs sampleSong = new Songs();
    sampleSong.setJirikiRank(JirikiRank.JIRIKI_S_PLUS);
    sampleSong.setSongName("みてみて☆こっちっち");
    sampleSong.setSongId("4");
    sampleSong.setContributor("エメラル");
    sampleSong.setInstrument("チェンバロ");
    songRepository.save(sampleSong);

    Songs sampleSong2 = new Songs();
    sampleSong2.setJirikiRank(JirikiRank.JIRIKI_B_PLUS);
    sampleSong2.setSongId("558");
    sampleSong2.setSongName("ミラクルペイント");
    sampleSong2.setContributor("タタナミ");
    sampleSong2.setInstrument("ピアノ①");
    songRepository.save(sampleSong2);
    Scores score = new Scores();
    score.setSongs(sampleSong2);
    score.setUsers(sampleUser);
    score.setScore(new ScoreValue(93));
    score.setScoreId(222);
    scoreRepository.save(score);
    Scores score2 = new Scores();
    score2.setScoreId(223);
    score2.setSongs(sampleSong);
    score2.setUsers(sampleUser2);
    score2.setScore(new ScoreValue(9));
    scoreRepository.save(score2);
  }

  private List<List<Object>> mockUsersRow() {
    Object[] row1 = {"u001", "u002", "u003"};
    Object[] row2 = {"妖怪1", "妖怪2", "妖怪3"};
    List<Object> row1AsList = Arrays.asList(row1);
    List<Object> row2AsList = Arrays.asList(row2);
    List<List<Object>> mockUsersRow = new ArrayList<List<Object>>();
    mockUsersRow.add(row1AsList);
    mockUsersRow.add(row2AsList);
    return mockUsersRow;
  }

  private List<List<Object>> mockSongsRow() {
    Object[] row1 = {
      "地力Ｓ＋",
      "みてみて☆こっちっち",
      "エメラル",
      "チェンバロ",
      "75.66",
      "96",
      "",
      "29",
      "みてみて☆こっちっちエメラルチェンバロ",
      "a11",
      "4"
    };
    Object[] row2 = {
      "地力Ａ＋", "ミラクルペイント", "タタナミ", "ピアノ①", "82.94", "96", "", "12", "ミラクルペイントタタナミピアノ①", "a31", "558"
    };
    Object[] row3 = {
      "地力Ｅ",
      "猿の入った温泉はヤバい",
      "へいおまち",
      "Ｄ・ギター①",
      "100",
      "100",
      "",
      "22",
      "猿の入った温泉はヤバいへいおまちＤ・ギター①",
      "a71",
      "567"
    };
    Object[] row4 = {
      "■■■■■",
      "俺の話を聞け",
      "どらごん",
      "ロックギター",
      "100",
      "100",
      "",
      "13",
      "俺の話を聞けどらごんロックギター",
      "e111",
      "3467"
    };
    Object[] row5 = {""};
    List<Object> row1AsList = Arrays.asList(row1);
    List<Object> row2AsList = Arrays.asList(row2);
    List<Object> row3AsList = Arrays.asList(row3);
    List<Object> row4AsList = Arrays.asList(row4);
    List<Object> row5AsList = Arrays.asList(row5);
    List<List<Object>> mockSongsRow = new ArrayList<List<Object>>();
    mockSongsRow.add(row1AsList);
    mockSongsRow.add(row2AsList);
    mockSongsRow.add(row3AsList);
    mockSongsRow.add(row4AsList);
    mockSongsRow.add(row5AsList);
    return mockSongsRow;
  }

  private List<List<Object>> mockScoresRow() {
    Object[] row1 = {"", "u001", "u002", "u003", "", ""};
    Object[] row2 = {"", "妖怪1", "妖怪2", "妖怪3", "", ""};
    Object[] row3 = {"4", "91", "93", "96", "", ""};
    Object[] row4 = {"558", "93", "90", "96", "", "."};
    Object[] row5 = {"567", "100", "たぶん100", "", "", ""};
    Object[] row6 = {"3467", "", "", "", "", ""};
    Object[] row7 = {};
    List<Object> row1AsList = Arrays.asList(row1);
    List<Object> row2AsList = Arrays.asList(row2);
    List<Object> row3AsList = Arrays.asList(row3);
    List<Object> row4AsList = Arrays.asList(row4);
    List<Object> row5AsList = Arrays.asList(row5);
    List<Object> row6AsList = Arrays.asList(row6);
    List<Object> row7AsList = Arrays.asList(row7);
    List<List<Object>> mockScoresRow = new ArrayList<List<Object>>();
    mockScoresRow.add(row1AsList);
    mockScoresRow.add(row2AsList);
    mockScoresRow.add(row3AsList);
    mockScoresRow.add(row4AsList);
    mockScoresRow.add(row5AsList);
    mockScoresRow.add(row6AsList);
    mockScoresRow.add(row7AsList);
    return mockScoresRow;
  }

  private List<ValueRange> valueRanges() {
    List<ValueRange> valueRanges = new ArrayList<>();
    ValueRange userRange = new ValueRange();
    userRange.setValues(mockUsersRow());
    valueRanges.add(userRange);
    ValueRange songRange = new ValueRange();
    songRange.setValues(mockSongsRow());
    valueRanges.add(songRange);
    ValueRange scoreRange = new ValueRange();
    scoreRange.setValues(mockScoresRow());
    valueRanges.add(scoreRange);
    return valueRanges;
  }

  @Transactional
  @Test
  public void スプレッドシートからの情報をもとにデータベースへの追加をする() throws Exception {
    when(sheetsService.getValuesFromSpreadSheet()).thenReturn(valueRanges());
    jirikiService.doGet();
    assertThat(songRepository.count()).isEqualTo(4);
    assertThat(scoreRepository.count()).isEqualTo(7);
    Users youkai2 = userRepository.findById("u002").get();
    assertThat(youkai2.getUserName()).isEqualTo("妖怪2");
    Songs miraclePaint = songRepository.findById("558").get();
    assertThat(miraclePaint.getJirikiRank()).isEqualTo(JirikiRank.JIRIKI_A_PLUS);
    Scores score =
        scoreRepository
            .findByUsers_UserIdAndSongs_SongId(youkai2.getUserId(), miraclePaint.getSongId())
            .get();
    assertThat(score.getScore().getScore().intValue()).isEqualTo(90);
  }
}

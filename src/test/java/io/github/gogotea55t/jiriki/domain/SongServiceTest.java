package io.github.gogotea55t.jiriki.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.gogotea55t.jiriki.domain.entity.Scores;
import io.github.gogotea55t.jiriki.domain.entity.Songs;
import io.github.gogotea55t.jiriki.domain.entity.Users;
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
import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;

@RunWith(SpringRunner.class)
@ActiveProfiles("unittest")
@SpringBootTest
public class SongServiceTest {
  @Autowired private UserRepository userRepository;

  @Autowired private SongRepository songRepository;

  @Autowired private ScoresRepository scoreRepository;

  @Rule public ExpectedException expectedException = ExpectedException.none();

  private SongService songService;
  PageRequest defaultPaging = new PageRequest(0, 20);
  private Map<String, String> query;

  @Before
  public void init() {
    songService = new SongService(songRepository, userRepository, scoreRepository);
    query = new HashMap<String, String>();
    SampleDatum sample = new SampleDatum();
    userRepository.deleteAll();
    songRepository.deleteAll();
    scoreRepository.deleteAll();

    userRepository.saveAll(sample.getUsers());
    songRepository.saveAll(sample.getSongs());
    scoreRepository.saveAll(sample.getScores());
  }

  @Test
  public void 全曲取得できる() throws Exception {
    List<SongsResponse> songs = songService.searchSongsByQuery(query, defaultPaging);
    assertThat(songs.size()).isEqualTo(3);
  }

  @Test
  public void ランダムに1曲取得できる() throws Exception {
    Map<String, String> query = new HashMap<String, String>();
    for (int i = 0; i < 100; i++) {
      SongsResponse songs = songService.getSongByRandom(query);
      assertThat(songs).isNotNull();
    }
  }

  @Test
  public void 地力を指定した中からランダムに1曲取得できる() throws Exception {
    Map<String, String> query = new HashMap<String, String>();
    query.put("jiriki", "地力Ｓ＋");
    for (int i = 0; i < 100; i++) {
      SongsResponse songs = songService.getSongByRandom(query);
      assertThat(songs).isNotNull();
    }
  }

  @Test
  public void ユーザIDを指定した中からランダムに1曲取得できる() throws Exception {
    Map<String, String> query = new HashMap<String, String>();
    query.put("user", "u001");
    for (int i = 0; i < 100; i++) {
      SongsResponse songs = songService.getSongByRandom(query);
      assertThat(songs).isNotNull();
    }
  }

  @Test
  public void 存在しないユーザIDから検索すると例外が発生する() throws Exception {
    Map<String, String> query = new HashMap<String, String>();
    query.put("user", "u099");
    expectedException.expect(SongsNotFoundException.class);
    expectedException.expectMessage("指定された条件に該当する曲がありません。");
    songService.getSongByRandom(query);
  }

  @Test
  public void 登録されていない地力ランクから検索すると例外が発生する() throws Exception {
    Map<String, String> query = new HashMap<String, String>();
    query.put("jiriki", "個人差Ｃ");
    expectedException.expect(SongsNotFoundException.class);
    expectedException.expectMessage("指定された条件に該当する曲がありません。");
    songService.getSongByRandom(query);
  }

  @Test
  public void ページングの設定は正しく反映される() throws Exception {
    List<SongsResponse> songs = songService.searchSongsByQuery(query, new PageRequest(0, 1));
    assertThat(songs.size()).isEqualTo(1);
  }

  @Test
  public void 楽曲名で部分一致検索できる() throws Exception {
    query.put("name", "みてみて");
    List<SongsResponse> songs = songService.searchSongsByQuery(query, defaultPaging);
    assertThat(songs.size()).isEqualTo(1);
  }

  @Test
  public void 投稿者名で部分一致検索できる() throws Exception {
    query.put("contributor", "エメ");
    List<SongsResponse> songs = songService.searchSongsByQuery(query, defaultPaging);
    assertThat(songs.size()).isEqualTo(1);
  }

  @Test
  public void 楽器で部分一致検索できる() throws Exception {
    query.put("instrument", "ピア");
    List<SongsResponse> songs = songService.searchSongsByQuery(query, defaultPaging);
    assertThat(songs.size()).isEqualTo(1);
  }

  @Test
  public void 地力で検索できる() throws Exception {
    query.put("jiriki", "地力Ａ＋");
    List<SongsResponse> songs = songService.searchSongsByQuery(query, defaultPaging);
    assertThat(songs.size()).isEqualTo(1);
    query.clear();
    query.put("jiriki", "地力Ｆ");
    assertThat(songService.searchSongsByQuery(query, defaultPaging).size()).isEqualTo(0);
  }

  @Test
  public void 存在しない楽曲名の場合何もないを返す() throws Exception {
    query.put("name", "こっち見んな");
    List<SongsResponse> songs = songService.searchSongsByQuery(query, defaultPaging);
    assertThat(songs.size()).isEqualTo(0);
  }

  @Test
  public void 存在しない投稿者名の場合何もないを返す() throws Exception {
    query.put("contributor", "ベースしもべ");
    List<SongsResponse> songs = songService.searchSongsByQuery(query, defaultPaging);
    assertThat(songs.size()).isEqualTo(0);
  }

  @Test
  public void 存在しない楽器の場合何もないを返す() throws Exception {
    query.put("instrument", "ネンリキ");
    List<SongsResponse> songs = songService.searchSongsByQuery(query, defaultPaging);
    assertThat(songs.size()).isEqualTo(0);
  }

  @Test
  public void 存在しない地力の場合は未決定で検索する() throws Exception {
    query.put("jiriki", "地力Ｖ");
    List<SongsResponse> songs = songService.searchSongsByQuery(query, defaultPaging);
    assertThat(songs.size()).isEqualTo(0);
  }

  @Test
  public void 楽曲IDで検索をかけられる() throws Exception {
    SongsResponse songs = songService.getSongBySongId("001");
    assertThat(songs.getSongName()).isEqualTo("みてみて☆こっちっち");
  }

  @Test
  public void 存在しない楽曲IDで検索をかけると何もないを返す() throws Exception {
    SongsResponse songs = songService.getSongBySongId("aaa");
    assertThat(songs).isNull();
  }

  /*
   * スコア系
   */

  @Test
  public void 存在しない楽曲IDでスコアを検索すると何もないが返ってくる() throws Exception {
    List<Score4SongResponse> scores = songService.getScoresBySongId("00000");
    assertThat(scores).isNull();
  }

  @Test
  public void 存在する楽曲IDでスコアを検索できる() throws Exception {
    List<Score4SongResponse> scores = songService.getScoresBySongId("001");
    assertThat(scores.size()).isEqualTo(2);
  }

  @Test
  public void 存在しない楽曲IDでスコアを検索すると何もないが返ってくるV2() throws Exception {
    List<Score4SongResponseV2> scores = songService.getScoresBySongIdV2("00000");
    assertThat(scores).isNull();
  }

  @Test
  public void 存在する楽曲IDでスコアを検索できるV2() throws Exception {
    List<Score4SongResponseV2> scores = songService.getScoresBySongIdV2("001");
    assertThat(scores.size()).isEqualTo(2);
  }

  @Test
  public void スコア検索をかけるとスコアが返ってくる() throws Exception {
    List<Score4UserResponse> scores = songService.searchScoresByQuery("u002", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(3);
  }

  @Test
  public void 楽曲名でスコア検索ができる() throws Exception {
    query.put("name", "ミラクルペイント");
    List<Score4UserResponse> scores = songService.searchScoresByQuery("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 楽曲名でスコア検索ができる_部分一致() throws Exception {
    query.put("name", "ミ");
    List<Score4UserResponse> scores = songService.searchScoresByQuery("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 楽曲名でスコア検索ができる_大文字小文字() throws Exception {
    query.put("name", "hISTory");
    List<Score4UserResponse> scores = songService.searchScoresByQuery("u002", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 投稿者名でスコア検索ができる() throws Exception {
    query.put("contributor", "エメラル");
    List<Score4UserResponse> scores = songService.searchScoresByQuery("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 投稿者名でスコア検索ができる_部分一致() throws Exception {
    query.put("contributor", "エメ");
    List<Score4UserResponse> scores = songService.searchScoresByQuery("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 楽器名でスコア情報を検索できる() throws Exception {
    query.put("instrument", "ピアノ");
    List<Score4UserResponse> scores = songService.searchScoresByQuery("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 楽器名でスコア情報を検索できる_部分一致() throws Exception {
    query.put("instrument", "ピ");
    List<Score4UserResponse> scores = songService.searchScoresByQuery("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 地力ランクでスコア情報を検索できる() throws Exception {
    query.put("jiriki", "地力Ａ＋");
    List<Score4UserResponse> scores = songService.searchScoresByQuery("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 存在しないユーザーでスコア検索をかける() throws Exception {
    expectedException.expect(IllegalArgumentException.class);
    songService.searchScoresByQuery("uqqqq", query, defaultPaging);
  }

  @Test
  public void スコア検索をかけるとスコアが返ってくるV2() throws Exception {
    List<Score4UserResponseV2> scores =
        songService.searchScoresByQueryV2("u002", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(3);
  }

  @Test
  public void 楽曲名でスコア検索ができるV2() throws Exception {
    query.put("name", "ミラクルペイント");
    List<Score4UserResponseV2> scores =
        songService.searchScoresByQueryV2("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 楽曲名でスコア検索ができるV2_部分一致() throws Exception {
    query.put("name", "ミ");
    List<Score4UserResponseV2> scores =
        songService.searchScoresByQueryV2("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 楽曲名でスコア検索ができるV2_大文字小文字() throws Exception {
    query.put("name", "hISTory");
    List<Score4UserResponseV2> scores =
        songService.searchScoresByQueryV2("u002", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 投稿者名でスコア検索ができるV2() throws Exception {
    query.put("contributor", "エメラル");
    List<Score4UserResponseV2> scores =
        songService.searchScoresByQueryV2("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 投稿者名でスコア検索ができるV2_部分一致() throws Exception {
    query.put("contributor", "エメ");
    List<Score4UserResponseV2> scores =
        songService.searchScoresByQueryV2("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 楽器名でスコア情報を検索できるV2() throws Exception {
    query.put("instrument", "ピアノ");
    List<Score4UserResponseV2> scores =
        songService.searchScoresByQueryV2("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 楽器名でスコア情報を検索できるV2_部分一致() throws Exception {
    query.put("instrument", "ピ");
    List<Score4UserResponseV2> scores =
        songService.searchScoresByQueryV2("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 地力ランクでスコア情報を検索できるV2() throws Exception {
    query.put("jiriki", "地力Ａ＋");
    List<Score4UserResponseV2> scores =
        songService.searchScoresByQueryV2("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 存在しないユーザーでスコア検索をかけるV2() throws Exception {
    expectedException.expect(IllegalArgumentException.class);
    songService.searchScoresByQueryV2("uqqqq", query, defaultPaging);
  }

  /*
   * Top系
   */
  @Test
  public void 上位3人がフルでいる曲の情報を取得する() throws Exception {
    Songs song = songRepository.findById("001").get();
    Users thirdMan = new Users();
    thirdMan.setUserId("u015");
    thirdMan.setUserName("第三の男");
    Users fourthMan = new Users();
    fourthMan.setUserId("u016");
    fourthMan.setUserName("第四の男");
    userRepository.save(thirdMan);
    userRepository.save(fourthMan);
    Scores thirdManScore = new Scores();
    thirdManScore.setUsers(thirdMan);
    thirdManScore.setSongs(song);
    thirdManScore.setScore(new ScoreValue("78"));
    Scores fourthManScore = new Scores();
    fourthManScore.setUsers(fourthMan);
    fourthManScore.setSongs(song);
    fourthManScore.setScore(new ScoreValue("76"));
    scoreRepository.save(thirdManScore);
    scoreRepository.save(fourthManScore);
    SongTopScoreResponse response = songService.getSongTopScore("001");
    assertThat(response.getTop().size()).isEqualTo(1);
    assertThat(response.getSecond().size()).isEqualTo(1);
    assertThat(response.getThird().size()).isEqualTo(1);
  }

  @Test
  public void トップに複数人数いる曲の情報を取得する() throws Exception {
    Songs song = songRepository.findById("001").get();
    Users thirdMan = new Users();
    thirdMan.setUserId("u015");
    thirdMan.setUserName("第三の男");
    Users fourthMan = new Users();
    fourthMan.setUserId("u016");
    fourthMan.setUserName("第四の男");
    userRepository.save(thirdMan);
    userRepository.save(fourthMan);
    Scores thirdManScore = new Scores();
    thirdManScore.setUsers(thirdMan);
    thirdManScore.setSongs(song);
    thirdManScore.setScore(new ScoreValue("100"));
    Scores fourthManScore = new Scores();
    fourthManScore.setUsers(fourthMan);
    fourthManScore.setSongs(song);
    fourthManScore.setScore(new ScoreValue("100"));
    scoreRepository.save(thirdManScore);
    scoreRepository.save(fourthManScore);
    SongTopScoreResponse response = songService.getSongTopScore("001");
    assertThat(response.getTop().size()).isEqualTo(2);
    assertThat(response.getSecond().size()).isEqualTo(1);
    assertThat(response.getThird().size()).isEqualTo(1);
  }

  @Test
  public void 存在しない曲のトップ情報を取得しようとする() throws Exception {
    SongTopScoreResponse response = songService.getSongTopScore("119");
    assertThat(response.getTop().size()).isEqualTo(0);
    assertThat(response.getSecond().size()).isEqualTo(0);
    assertThat(response.getThird().size()).isEqualTo(0);
  }
  
  @Test
  public void 楽曲ごとの統計情報を取得できる() throws Exception {
	StatisticResponse response = songService.getSongStat("001");
	assertThat(response.getGold()).isEqualTo(0);
	assertThat(response.getSilver()).isEqualTo(2);
	assertThat(response.getBronze()).isEqualTo(0);
	assertThat(response.getGray()).isEqualTo(0);
	assertThat(response.getBlue()).isEqualTo(0);
	assertThat(response.getNone()).isEqualTo(0);
  }
}

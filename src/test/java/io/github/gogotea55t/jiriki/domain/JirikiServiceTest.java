package io.github.gogotea55t.jiriki.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.github.gogotea55t.jiriki.domain.entity.TwitterUsers;
import io.github.gogotea55t.jiriki.domain.repository.ScoresRepository;
import io.github.gogotea55t.jiriki.domain.repository.SongRepository;
import io.github.gogotea55t.jiriki.domain.repository.TwitterUsersRepository;
import io.github.gogotea55t.jiriki.domain.repository.UserRepository;
import io.github.gogotea55t.jiriki.domain.request.PageRequest;
import io.github.gogotea55t.jiriki.domain.request.TwitterUsersRequest;

@RunWith(SpringRunner.class)
@ActiveProfiles("unittest")
@SpringBootTest
public class JirikiServiceTest {
  @Autowired private GoogleSpreadSheetConfig sheetConfig;

  @Autowired private GoogleSheetsService sheetService;

  @Autowired private UserRepository userRepository;

  @Autowired private SongRepository songRepository;

  @Autowired private ScoresRepository scoreRepository;

  @Autowired private TwitterUsersRepository twiRepository;
  
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

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
            twiRepository);
    SampleDatum sample = new SampleDatum();
    userRepository.deleteAll();
    songRepository.deleteAll();
    scoreRepository.deleteAll();
    twiRepository.deleteAll();

    userRepository.saveAll(sample.getUsers());
    songRepository.saveAll(sample.getSongs());
    scoreRepository.saveAll(sample.getScores());
    twiRepository.save(new TwitterUsers("twitterId", sample.getUsers().get(1)));
    query = new HashMap<String,String>();
  }

  /*
   * ユーザー系
   */

  @Test
  public void ユーザーの一覧を正常に取得できる() throws Exception {
    assertThat(jirikiService.getAllPlayer().size()).isEqualTo(2);
  }

  @Test
  public void ユーザー名で検索をかけることができる() throws Exception {
    List<UserResponse> searchResult = jirikiService.getPlayerByName("妖怪1");
    assertThat(searchResult.size()).isEqualTo(1);
    assertThat(searchResult.get(0).getUserName()).isEqualTo("妖怪1");
    assertThat(searchResult.get(0).getUserId()).isEqualTo("u001");
  }

  @Test
  public void ユーザー名は部分一致で検索する() throws Exception {
    List<UserResponse> searchResult = jirikiService.getPlayerByName("妖怪");
    assertThat(searchResult.size()).isEqualTo(2);
  }

  @Test
  public void 検索結果は0件でも問題ない() throws Exception {
    List<UserResponse> searchResult = jirikiService.getPlayerByName("人間");
    assertThat(searchResult.size()).isEqualTo(0);
  }

  @Test
  public void ユーザーIDで検索をかけることができる() throws Exception {
    UserResponse searchResult = jirikiService.getPlayerById("u001");
    assertThat(searchResult.getUserName()).isEqualTo("妖怪1");
  }

  @Test
  public void 存在しないユーザーIDで検索をしたら何もないが返ってくる() throws Exception {
    UserResponse searchResult = jirikiService.getPlayerById("human");
    assertThat(searchResult).isNull();
  }

  @Test
  public void twitterのIDとユーザーの紐づけができ登録したものを閲覧できる() throws Exception {
    TwitterUsersRequest testRequest = new TwitterUsersRequest();
    String testTwitterId = "aaaaaaaaaa";
    testRequest.setUserId("u001");
    testRequest.setTwitterUserId(testTwitterId);
    jirikiService.addNewLinkBetweenUserAndTwitterUser(testRequest);

    Optional<TwitterUsers> putResult = twiRepository.findById(testTwitterId);
    assertThat(putResult.isPresent()).isTrue();
    assertThat(putResult.get().getTwitterUserId()).isEqualTo(testTwitterId);
    assertThat(putResult.get().getUsers().getUserId()).isEqualTo("u001");

    UserResponse getResult = jirikiService.findPlayerByTwitterId(testTwitterId);
    assertThat(getResult.getUserId()).isEqualTo("u001");
  }

  @Test
  public void 存在しないtwitterアカウントを検索するとnullが返ってくる() throws Exception {
    assertThat(jirikiService.findPlayerByTwitterId("hogehoge")).isNull();
  }

  @Test(expected = NullPointerException.class)
  public void 存在しないユーザーに対してTwitterアカウントを紐づけようとすると例外が出る() throws Exception {
    TwitterUsersRequest testRequest = new TwitterUsersRequest();
    testRequest.setUserId("hogehoge");
    testRequest.setTwitterUserId("hogehogehoge");

    jirikiService.addNewLinkBetweenUserAndTwitterUser(testRequest);
  }

  @Test
  public void すでに登録済みだったTwitterIdを別のアカウントに紐づけようとすると新しいアカウントに紐づく() throws Exception {
    String testTwitterId = "twitterId";

    TwitterUsersRequest testRequest = new TwitterUsersRequest();
    testRequest.setTwitterUserId(testTwitterId);
    testRequest.setUserId("u001");

    UserResponse putResult = jirikiService.addNewLinkBetweenUserAndTwitterUser(testRequest);
    assertThat(putResult.getUserId()).isEqualTo("u001");

    UserResponse getResult = jirikiService.findPlayerByTwitterId(testTwitterId);
    assertThat(getResult.getUserId()).isEqualTo("u001");

    assertThat(userRepository.findById("u002").isPresent()).isTrue();
  }

  /*
   * 楽曲系
   */

  @Test
  public void 全曲取得できる() throws Exception {
    List<SongsResponse> songs = jirikiService.searchSongsByQuery(query, defaultPaging);
    assertThat(songs.size()).isEqualTo(2);
  }

  @Test
  public void ページングの設定は正しく反映される() throws Exception {
    List<SongsResponse> songs = jirikiService.searchSongsByQuery(query, new PageRequest(0, 1));
    assertThat(songs.size()).isEqualTo(1);
  }

  @Test
  public void 楽曲名で部分一致検索できる() throws Exception {
    query.put("name", "みてみて");
    List<SongsResponse> songs = jirikiService.searchSongsByQuery(query, defaultPaging);
    assertThat(songs.size()).isEqualTo(1);
  }

  @Test
  public void 投稿者名で部分一致検索できる() throws Exception {
    query.put("contributor", "エメ");
    List<SongsResponse> songs = jirikiService.searchSongsByQuery(query, defaultPaging);
    assertThat(songs.size()).isEqualTo(1);
  }

  @Test
  public void 楽器で部分一致検索できる() throws Exception {
    query.put("instrument", "ピア");
    List<SongsResponse> songs = jirikiService.searchSongsByQuery(query, defaultPaging);
    assertThat(songs.size()).isEqualTo(1);
  }

  @Test
  public void 地力で検索できる() throws Exception {
    query.put("jiriki", "地力Ａ＋");
    List<SongsResponse> songs = jirikiService.searchSongsByQuery(query, defaultPaging);
    assertThat(songs.size()).isEqualTo(1);
    query.clear();
    query.put("jiriki", "地力Ｆ");
    assertThat(jirikiService.searchSongsByQuery(query, defaultPaging).size()).isEqualTo(0);
  }

  @Test
  public void 存在しない楽曲名の場合何もないを返す() throws Exception {
    query.put("name", "こっち見んな");
    List<SongsResponse> songs = jirikiService.searchSongsByQuery(query, defaultPaging);
    assertThat(songs.size()).isEqualTo(0);
  }

  @Test
  public void 存在しない投稿者名の場合何もないを返す() throws Exception {
    query.put("contributor", "ベースしもべ");
    List<SongsResponse> songs = jirikiService.searchSongsByQuery(query, defaultPaging);
    assertThat(songs.size()).isEqualTo(0);
  }

  @Test
  public void 存在しない楽器の場合何もないを返す() throws Exception {
    query.put("instrument", "ネンリキ");
    List<SongsResponse> songs = jirikiService.searchSongsByQuery(query, defaultPaging);
    assertThat(songs.size()).isEqualTo(0);
  }

  @Test
  public void 存在しない地力の場合は未決定で検索する() throws Exception {
    query.put("jiriki", "地力Ｖ");
    List<SongsResponse> songs = jirikiService.searchSongsByQuery(query, defaultPaging);
    assertThat(songs.size()).isEqualTo(0);
  }

  @Test
  public void 楽曲IDで検索をかけられる() throws Exception {
    SongsResponse songs = jirikiService.getSongBySongId("001");
    assertThat(songs.getSongName()).isEqualTo("みてみて☆こっちっち");
  }

  @Test
  public void 存在しない楽曲IDで検索をかけると何もないを返す() throws Exception {
    SongsResponse songs = jirikiService.getSongBySongId("aaa");
    assertThat(songs).isNull();
  }

  /*
   * スコア系
   */

  @Test
  public void 存在しない楽曲IDでスコアを検索すると何もないが返ってくる() throws Exception {
    List<Score4SongResponse> scores = jirikiService.getScoresBySongId("00000");
    assertThat(scores).isNull();
  }

  @Test
  public void スコア検索をかけるとスコアが返ってくる() throws Exception {
    List<Score4UserResponse> scores =
        jirikiService.searchScoresByQuery("u002", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(2);
  }

  @Test
  public void 楽曲名でスコア検索ができる() throws Exception {
    query.put("name", "ミラクルペイント");
    List<Score4UserResponse> scores =
        jirikiService.searchScoresByQuery("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 楽曲名でスコア検索ができる_部分一致() throws Exception {
    query.put("name", "ミ");
    List<Score4UserResponse> scores =
        jirikiService.searchScoresByQuery("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 投稿者名でスコア検索ができる() throws Exception {
    query.put("contributor", "エメラル");
    List<Score4UserResponse> scores =
        jirikiService.searchScoresByQuery("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 投稿者名でスコア検索ができる_部分一致() throws Exception {
    query.put("contributor", "エメ");
    List<Score4UserResponse> scores =
        jirikiService.searchScoresByQuery("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 楽器名でスコア情報を検索できる() throws Exception {
    query.put("instrument", "ピアノ");
    List<Score4UserResponse> scores =
        jirikiService.searchScoresByQuery("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 楽器名でスコア情報を検索できる_部分一致() throws Exception {
    query.put("instrument", "ピ");
    List<Score4UserResponse> scores =
        jirikiService.searchScoresByQuery("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 地力ランクでスコア情報を検索できる() throws Exception {
    query.put("jiriki", "地力Ａ＋");
    List<Score4UserResponse> scores =
        jirikiService.searchScoresByQuery("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 存在しないユーザーでスコア検索をかける() throws Exception {
	expectedException.expect(IllegalArgumentException.class);
    jirikiService.searchScoresByQuery("uqqqq",query, defaultPaging);
  }
}

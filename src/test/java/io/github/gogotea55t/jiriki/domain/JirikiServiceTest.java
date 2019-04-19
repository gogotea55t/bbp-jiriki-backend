package io.github.gogotea55t.jiriki.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.github.gogotea55t.jiriki.domain.repository.ScoresRepository;
import io.github.gogotea55t.jiriki.domain.repository.SongRepository;
import io.github.gogotea55t.jiriki.domain.repository.UserRepository;

@RunWith(SpringRunner.class)
@ActiveProfiles("unittest")
@SpringBootTest
public class JirikiServiceTest {
  @Autowired private GoogleSpreadSheetConfig sheetConfig;

  @Autowired private UserRepository userRepository;

  @Autowired private SongRepository songRepository;

  @Autowired private ScoresRepository scoreRepository;

  private JirikiService jirikiService;

  Pageable defaultPaging = PageRequest.of(0, 20);

  @Transactional
  @Before
  public void init() {
    jirikiService = new JirikiService(sheetConfig, userRepository, songRepository, scoreRepository);
    SampleDatum sample = new SampleDatum();
    userRepository.deleteAll();
    songRepository.deleteAll();
    scoreRepository.deleteAll();
    
    userRepository.saveAll(sample.getUsers());
    songRepository.saveAll(sample.getSongs());
    scoreRepository.saveAll(sample.getScores());
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
  public void ユーザー名は完全一致で検索する() throws Exception {
    List<UserResponse> searchResult = jirikiService.getPlayerByName("妖怪");
    assertThat(searchResult.size()).isEqualTo(0);
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

  /*
   * 楽曲系
   */

  @Test
  public void 全曲取得できる() throws Exception {
    List<SongsResponse> songs = jirikiService.getAllSongs(defaultPaging);
    assertThat(songs.size()).isEqualTo(2);
  }
  
  @Test
  public void ページングの設定は正しく反映される() throws Exception {
	List<SongsResponse> songs = jirikiService.getAllSongs(PageRequest.of(0, 1));
	assertThat(songs.size()).isEqualTo(1);
  }

  @Test
  public void 楽曲名で部分一致検索できる() throws Exception {
    List<SongsResponse> songs = jirikiService.getSongBySongName("みてみて", defaultPaging);
    assertThat(songs.size()).isEqualTo(1);
  }

  @Test
  public void 投稿者名で部分一致検索できる() throws Exception {
    List<SongsResponse> songs = jirikiService.getSongByContributor("エメ", defaultPaging);
    assertThat(songs.size()).isEqualTo(1);
  }

  @Test
  public void 楽器で部分一致検索できる() throws Exception {
    List<SongsResponse> songs = jirikiService.getSongByInstrument("ピア", defaultPaging);
    assertThat(songs.size()).isEqualTo(1);
  }

  @Test
  public void 存在しない楽曲名の場合何もないを返す() throws Exception {
    List<SongsResponse> songs = jirikiService.getSongBySongName("こっち見んな", defaultPaging);
    assertThat(songs.size()).isEqualTo(0);
  }
  
  @Test
  public void 存在しない投稿者名の場合何もないを返す() throws Exception {
    List<SongsResponse> songs = jirikiService.getSongByContributor("ベースしもべ", defaultPaging);
    assertThat(songs.size()).isEqualTo(0);
  }

  @Test
  public void 存在しない楽器の場合何もないを返す() throws Exception {
    List<SongsResponse> songs = jirikiService.getSongByInstrument("ネンリキ", defaultPaging);
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
//  @Transactional
//  @Test
//  public void 楽曲IDからスコアを取得できる() throws Exception {
//	List<Score4SongResponse> scores = jirikiService.getScoresBySongId("001");
//	assertThat(scores.size()).isEqualTo(2);
//	List<Score4SongResponse> scores2 = jirikiService.getScoresBySongId("002");
//	assertThat(scores2.size()).isEqualTo(1);
//  }
//  
//  @Transactional
//  @Test
//  public void ユーザーIDからスコアを取得できる() throws Exception {
//	List<Score4UserResponse> scores = jirikiService.getScoresByUserId("u001");
//	assertThat(scores.size()).isEqualTo(2);
//  }
  
  @Test
  public void 存在しない楽曲IDでスコアを検索すると何もないが返ってくる() throws Exception {
	List<Score4SongResponse> scores = jirikiService.getScoresBySongId("00000");
	assertThat(scores).isNull();
  }
  
  @Test
  public void 空行付きリクエストをすると空行も一緒に帰ってくる() throws Exception {
	List<Score4UserResponse> scores = jirikiService.getScoresByUserIdWithEmpty("u002", defaultPaging);
	assertThat(scores.size()).isEqualTo(2);
  }
}

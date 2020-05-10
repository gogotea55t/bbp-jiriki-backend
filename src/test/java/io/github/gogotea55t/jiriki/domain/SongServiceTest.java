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

import io.github.gogotea55t.jiriki.domain.repository.ScoresRepository;
import io.github.gogotea55t.jiriki.domain.repository.SongRepository;
import io.github.gogotea55t.jiriki.domain.repository.UserRepository;
import io.github.gogotea55t.jiriki.domain.request.PageRequest;
import io.github.gogotea55t.jiriki.domain.response.Score4SongResponse;
import io.github.gogotea55t.jiriki.domain.response.Score4SongResponseV2;
import io.github.gogotea55t.jiriki.domain.response.Score4UserResponse;
import io.github.gogotea55t.jiriki.domain.response.Score4UserResponseV2;
import io.github.gogotea55t.jiriki.domain.response.SongsResponse;

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
    for (int i = 0; i < 100; i++) {
      SongsResponse songs = songService.getSongByRandom();
      assertThat(songs).isNotNull();
    }
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
    List<Score4UserResponse> scores =
        songService.searchScoresByQuery("u002", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(3);
  }

  @Test
  public void 楽曲名でスコア検索ができる() throws Exception {
    query.put("name", "ミラクルペイント");
    List<Score4UserResponse> scores =
        songService.searchScoresByQuery("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 楽曲名でスコア検索ができる_部分一致() throws Exception {
    query.put("name", "ミ");
    List<Score4UserResponse> scores =
        songService.searchScoresByQuery("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 楽曲名でスコア検索ができる_大文字小文字() throws Exception {
    query.put("name", "hISTory");
    List<Score4UserResponse> scores =
        songService.searchScoresByQuery("u002", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 投稿者名でスコア検索ができる() throws Exception {
    query.put("contributor", "エメラル");
    List<Score4UserResponse> scores =
        songService.searchScoresByQuery("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 投稿者名でスコア検索ができる_部分一致() throws Exception {
    query.put("contributor", "エメ");
    List<Score4UserResponse> scores =
        songService.searchScoresByQuery("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 楽器名でスコア情報を検索できる() throws Exception {
    query.put("instrument", "ピアノ");
    List<Score4UserResponse> scores =
        songService.searchScoresByQuery("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 楽器名でスコア情報を検索できる_部分一致() throws Exception {
    query.put("instrument", "ピ");
    List<Score4UserResponse> scores =
        songService.searchScoresByQuery("u001", query, defaultPaging);
    assertThat(scores.size()).isEqualTo(1);
  }

  @Test
  public void 地力ランクでスコア情報を検索できる() throws Exception {
    query.put("jiriki", "地力Ａ＋");
    List<Score4UserResponse> scores =
        songService.searchScoresByQuery("u001", query, defaultPaging);
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
}

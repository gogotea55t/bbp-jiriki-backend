package io.github.gogotea55t.jiriki.ui;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.ibatis.session.RowBounds;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.gogotea55t.jiriki.domain.SampleDatum;
import io.github.gogotea55t.jiriki.domain.SongService;
import io.github.gogotea55t.jiriki.domain.request.PageRequest;
import io.github.gogotea55t.jiriki.domain.response.Score4SongResponse;
import io.github.gogotea55t.jiriki.domain.response.Score4SongResponseV2;
import io.github.gogotea55t.jiriki.domain.response.Score4UserResponse;
import io.github.gogotea55t.jiriki.domain.response.Score4UserResponseV2;
import io.github.gogotea55t.jiriki.domain.response.SongTopScoreResponse;
import io.github.gogotea55t.jiriki.domain.response.SongsResponse;
import io.github.gogotea55t.jiriki.domain.response.UserResponse;

@RunWith(SpringRunner.class)
@ActiveProfiles("unittest")
@SpringBootTest
public class SongControllerTest {
  private MockMvc mockMvc;
  @MockBean SongService mockService;
  @Autowired ObjectMapper objectMapper;
  @Autowired ExceptionHandlerAdvice exceptionHandler;

  private SongController controller;
  private PageRequest defaultPaging = new PageRequest(0, 20);

  private Map<String, String> query;
  private SampleDatum sample = new SampleDatum();
  List<SongsResponse> mockSongsResponse =
      sample.getSongs().stream().map(s -> SongsResponse.of(s)).collect(Collectors.toList());

  List<Score4SongResponse> mockScore4SongResponse =
      sample.getScores().stream().map(s -> Score4SongResponse.of(s)).collect(Collectors.toList());

  List<Score4UserResponse> mockScore4UserResponse =
      sample.getScores().stream().map(s -> Score4UserResponse.of(s)).collect(Collectors.toList());

  List<UserResponse> mockUserResponse =
      sample.getUsers().stream().map(u -> UserResponse.of(u)).collect(Collectors.toList());

  List<Score4SongResponseV2> mockScore4SongResponseV2 =
      sample
          .getScores()
          .stream()
          .map(
              u -> {
                Score4SongResponseV2 sc = new Score4SongResponseV2();
                sc.setUserId(u.getUsers().getUserId());
                sc.setUserName(u.getUsers().getUserName());
                sc.setScore(u.getScore());
                return sc;
              })
          .collect(Collectors.toList());

  List<Score4UserResponseV2> mockScore4UserResponseV2 =
      sample
          .getScores()
          .stream()
          .map(
              u -> {
                Score4UserResponseV2 su = new Score4UserResponseV2();
                su.setSongId(u.getSongs().getSongId());
                su.setJirikiRank(u.getSongs().getJirikiRank());
                su.setInstrument(u.getSongs().getInstrument());
                su.setContributor(u.getSongs().getContributor());
                su.setSongName(u.getSongs().getSongName());
                su.setScore(u.getScore());
                su.setAverage(u.getScore());
                su.setMax(u.getScore());
                return su;
              })
          .collect(Collectors.toList());

  @Before
  public void init() {
    controller = new SongController(mockService);
    query = new HashMap<String, String>();
    mockMvc = MockMvcBuilders.standaloneSetup(controller, exceptionHandler).build();
  }

  @Test
  public void 楽曲情報が取得できる() throws Exception {
    when(mockService.searchSongsByQuery(query, defaultPaging)).thenReturn(mockSongsResponse);

    mockMvc
        .perform(get(new URI("/v1/songs")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockSongsResponse)));
  }

  @Test
  public void 楽曲情報がランダムに取得できる() throws Exception {
    Map<String, String> query = new HashMap<String, String>();
    when(mockService.getSongByRandom(query)).thenReturn(mockSongsResponse.get(0));
    mockMvc
        .perform(get(new URI("/v1/songs/random")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockSongsResponse.get(0))));
  }

  @Test
  public void 楽曲情報がランダムに取得できなかったときは4004() throws Exception {
    Map<String, String> query = new HashMap<String, String>();
    when(mockService.getSongByRandom(query)).thenReturn(null);
    mockMvc.perform(get(new URI("/v1/songs/random"))).andExpect(status().is(404));
  }

  @Test
  public void 楽曲名で検索して楽曲情報が取得できる() throws Exception {
    query.put("name", "カミサマネジマキ");
    RowBounds a = new RowBounds(0, 20);
    System.out.println(a.equals(new RowBounds(0, 20)));
    when(mockService.searchSongsByQuery(query, defaultPaging)).thenReturn(mockSongsResponse);
    System.out.println(mockSongsResponse);
    mockMvc
        .perform(get(new URI("/v1/songs?name=カミサマネジマキ")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockSongsResponse)));
  }

  @Test
  public void 投稿者名で検索して楽曲情報が取得できる() throws Exception {
    query.put("contributor", "ミラ");
    when(mockService.searchSongsByQuery(query, defaultPaging)).thenReturn(mockSongsResponse);
    mockMvc
        .perform(get(new URI("/v1/songs?contributor=ミラ")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockSongsResponse)));
  }

  @Test
  public void 楽器名で検索して楽曲情報が取得できる() throws Exception {
    query.put("instrument", "ロックオルガン");
    when(mockService.searchSongsByQuery(query, defaultPaging)).thenReturn(mockSongsResponse);
    mockMvc
        .perform(get(new URI("/v1/songs?instrument=ロックオルガン")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockSongsResponse)));
  }

  @Test
  public void 地力ランクを指定して検索ができる() throws Exception {
    query.put("jiriki", "地力Ｅ");
    when(mockService.searchSongsByQuery(query, defaultPaging)).thenReturn(mockSongsResponse);
    mockMvc
        .perform(get(new URI("/v1/songs?jiriki=地力Ｅ")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockSongsResponse)));
  }

  @Test
  public void 地力ランクとしてよくわからない文字列が来てもとりあえずその文字列で検索する() throws Exception {
    query.put("jiriki", "地力AAA");
    when(mockService.searchSongsByQuery(query, defaultPaging)).thenReturn(mockSongsResponse);

    mockMvc
        .perform(get(new URI("/v1/songs?jiriki=地力AAA")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockSongsResponse)));
  }

  @Test
  public void 楽曲IDを指定して楽曲情報を取得できる() throws Exception {
    when(mockService.getSongBySongId("001")).thenReturn(SongsResponse.of(sample.getSongs().get(0)));
    mockMvc.perform(get(new URI("/v1/songs/001"))).andExpect(status().isOk());
  }

  @Test
  public void 存在しない楽曲IDを指定すると楽曲情報が取得できない() throws Exception {
    when(mockService.getSongBySongId("004")).thenReturn(null);
    mockMvc.perform(get(new URI("/v1/songs/004"))).andExpect(status().is4xxClientError());
  }

  @Test
  public void 楽曲IDを指定してスコア情報を取得できる() throws Exception {
    when(mockService.getScoresBySongId("001")).thenReturn(mockScore4SongResponse);

    mockMvc
        .perform(get(new URI("/v1/songs/001/scores")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockScore4SongResponse)));
  }

  @Test
  public void 楽曲IDを指定してスコア情報を取得できるv2() throws Exception {
    when(mockService.getScoresBySongIdV2("001")).thenReturn(mockScore4SongResponseV2);

    mockMvc
        .perform(get(new URI("/v2/songs/001/scores")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockScore4SongResponseV2)));
  }

  @Test
  public void 存在しない楽曲IDを指定するとスコア情報が虚無v2() throws Exception {
    when(mockService.getScoresBySongIdV2("004")).thenReturn(null);

    mockMvc.perform(get(new URI("/v2/songs/004/scores"))).andExpect(status().is(404));
  }

  @Test
  public void 存在しない楽曲IDを指定するとスコア情報が取得できない() throws Exception {
    when(mockService.getScoresBySongId("004")).thenReturn(null);
    mockMvc.perform(get(new URI("/v1/songs/004/scores"))).andExpect(status().is4xxClientError());
  }

  @Test
  public void プレイヤーIDを指定してスコア情報を取得できる() throws Exception {
    when(mockService.searchScoresByQuery("u001", query, defaultPaging))
        .thenReturn(mockScore4UserResponse);
    mockMvc
        .perform(get(new URI("/v1/players/u001/scores")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockScore4UserResponse)));
  }

  @Test
  public void 存在しないプレイヤーIDを指定するとスコア情報が取得できない() throws Exception {
    when(mockService.searchScoresByQuery("human", query, defaultPaging)).thenReturn(null);
    mockMvc
        .perform(get(new URI("/v1/players/human/scores")))
        .andExpect(status().is4xxClientError());
  }

  private String toJson(Object object) throws JsonProcessingException {
    return objectMapper.writeValueAsString(object);
  }

  @Test
  public void プレイヤーIDと楽曲名を指定してスコア情報を取得できる() throws Exception {
    query.put("name", "みてみて☆こっちっち");
    when(mockService.searchScoresByQuery("u001", query, defaultPaging))
        .thenReturn(mockScore4UserResponse);
    mockMvc
        .perform(get(new URI("/v1/players/u001/scores?name=みてみて☆こっちっち")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockScore4UserResponse)));
  }

  @Test
  public void プレイヤーIDを投稿者名を指定してスコア情報を取得できる() throws Exception {
    query.put("contributor", "エメラル");
    when(mockService.searchScoresByQuery("u001", query, defaultPaging))
        .thenReturn(mockScore4UserResponse);
    mockMvc
        .perform(get(new URI("/v1/players/u001/scores?contributor=エメラル")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockScore4UserResponse)));
  }

  @Test
  public void プレイヤーIDと楽器名を指定してスコア情報を取得できる() throws Exception {
    query.put("instrument", "ピアノ");
    when(mockService.searchScoresByQuery("u001", query, defaultPaging))
        .thenReturn(mockScore4UserResponse);
    mockMvc
        .perform(get(new URI("/v1/players/u001/scores?instrument=ピアノ")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockScore4UserResponse)));
  }

  @Test
  public void プレイヤーIDと地力ランクを指定してスコア情報を取得できる() throws Exception {
    query.put("jiriki", "地力Ｄ");
    when(mockService.searchScoresByQuery("u001", query, defaultPaging))
        .thenReturn(mockScore4UserResponse);
    mockMvc
        .perform(get(new URI("/v1/players/u001/scores?jiriki=地力Ｄ")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockScore4UserResponse)));
  }

  @Test
  public void プレイヤーIDを指定してスコア情報を取得できるV2() throws Exception {
    when(mockService.searchScoresByQueryV2("u001", query, defaultPaging))
        .thenReturn(mockScore4UserResponseV2);
    mockMvc
        .perform(get(new URI("/v2/players/u001/scores")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockScore4UserResponse)));
  }

  @Test
  public void 存在しないプレイヤーIDを指定するとスコア情報が取得できないV2() throws Exception {
    when(mockService.searchScoresByQueryV2("human", query, defaultPaging)).thenReturn(null);
    mockMvc
        .perform(get(new URI("/v2/players/human/scores")))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void プレイヤーIDと楽曲名を指定してスコア情報を取得できるV2() throws Exception {
    query.put("name", "みてみて☆こっちっち");
    when(mockService.searchScoresByQueryV2("u001", query, defaultPaging))
        .thenReturn(mockScore4UserResponseV2);
    mockMvc
        .perform(get(new URI("/v2/players/u001/scores?name=みてみて☆こっちっち")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockScore4UserResponse)));
  }

  @Test
  public void プレイヤーIDを投稿者名を指定してスコア情報を取得できるV2() throws Exception {
    query.put("contributor", "エメラル");
    when(mockService.searchScoresByQueryV2("u001", query, defaultPaging))
        .thenReturn(mockScore4UserResponseV2);
    mockMvc
        .perform(get(new URI("/v2/players/u001/scores?contributor=エメラル")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockScore4UserResponse)));
  }

  @Test
  public void プレイヤーIDと楽器名を指定してスコア情報を取得できるV2() throws Exception {
    query.put("instrument", "ピアノ");
    when(mockService.searchScoresByQueryV2("u001", query, defaultPaging))
        .thenReturn(mockScore4UserResponseV2);
    mockMvc
        .perform(get(new URI("/v2/players/u001/scores?instrument=ピアノ")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockScore4UserResponse)));
  }

  @Test
  public void プレイヤーIDと地力ランクを指定してスコア情報を取得できるV2() throws Exception {
    query.put("jiriki", "地力Ｄ");
    when(mockService.searchScoresByQueryV2("u001", query, defaultPaging))
        .thenReturn(mockScore4UserResponseV2);
    mockMvc
        .perform(get(new URI("/v2/players/u001/scores?jiriki=地力Ｄ")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockScore4UserResponse)));
  }

  @Test
  public void 平均点一覧を取得できる() throws Exception {
    when(mockService.searchAverageScoresByQuery(query, defaultPaging))
        .thenReturn(mockScore4UserResponse);
    mockMvc
        .perform(get(new URI("/v1/players/average/scores")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockScore4UserResponse)));
  }

  @Test
  public void 投稿者名で検索して平均点一覧を取得できる() throws Exception {
    query.put("contributor", "エメラル");

    when(mockService.searchAverageScoresByQuery(query, defaultPaging))
        .thenReturn(mockScore4UserResponse);
    mockMvc
        .perform(get(new URI("/v1/players/average/scores?contributor=エメラル")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockScore4UserResponse)));
  }

  @Test
  public void 曲名で検索して平均点一覧を取得できる() throws Exception {
    query.put("name", "みてみて☆こっちっち");

    when(mockService.searchAverageScoresByQuery(query, defaultPaging))
        .thenReturn(mockScore4UserResponse);
    mockMvc
        .perform(get(new URI("/v1/players/average/scores?name=みてみて☆こっちっち")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockScore4UserResponse)));
  }

  @Test
  public void 楽器名で検索して平均点一覧を取得できる() throws Exception {
    query.put("instrument", "ロックオルガン");

    when(mockService.searchAverageScoresByQuery(query, defaultPaging))
        .thenReturn(mockScore4UserResponse);
    mockMvc
        .perform(get(new URI("/v1/players/average/scores?instrument=ロックオルガン")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockScore4UserResponse)));
  }

  @Test
  public void 地力で検索して平均点一覧を取得できる() throws Exception {
    query.put("jiriki", "地力Ａ");

    when(mockService.searchAverageScoresByQuery(query, defaultPaging))
        .thenReturn(mockScore4UserResponse);
    mockMvc
        .perform(get(new URI("/v1/players/average/scores?jiriki=地力Ａ")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockScore4UserResponse)));
  }

  @Test
  public void 楽曲のトップスコア情報を取得できる() throws Exception {
    when(mockService.getSongTopScore("5")).thenReturn(new SongTopScoreResponse());
    mockMvc.perform(get(new URI("/v2/songs/5/top"))).andExpect(status().isOk());
  }
}

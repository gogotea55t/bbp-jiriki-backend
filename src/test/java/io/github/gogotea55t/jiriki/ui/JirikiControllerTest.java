package io.github.gogotea55t.jiriki.ui;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.gogotea55t.jiriki.domain.JirikiService;
import io.github.gogotea55t.jiriki.domain.SampleDatum;
import io.github.gogotea55t.jiriki.domain.Score4SongResponse;
import io.github.gogotea55t.jiriki.domain.Score4UserResponse;
import io.github.gogotea55t.jiriki.domain.SongsResponse;
import io.github.gogotea55t.jiriki.domain.UserResponse;
import io.github.gogotea55t.jiriki.domain.request.TwitterUsersRequest;
import io.github.gogotea55t.jiriki.domain.vo.JirikiRank;

@RunWith(SpringRunner.class)
@ActiveProfiles("unittest")
@SpringBootTest
public class JirikiControllerTest {
  private MockMvc mockMvc;

  @MockBean JirikiService mockService;

  @Autowired ObjectMapper objectMapper;

  private JirikiController controller;

  private Pageable defaultPaging =
      PageRequest.of(0, 20, Sort.by(Order.asc("jirikiRank"), Order.asc("songId")));

  private SampleDatum sample = new SampleDatum();
  List<SongsResponse> mockSongsResponse =
      sample.getSongs().stream().map(s -> SongsResponse.of(s)).collect(Collectors.toList());

  List<Score4SongResponse> mockScore4SongResponse =
      sample.getScores().stream().map(s -> Score4SongResponse.of(s)).collect(Collectors.toList());

  List<Score4UserResponse> mockScore4UserResponse =
      sample.getScores().stream().map(s -> Score4UserResponse.of(s)).collect(Collectors.toList());

  List<UserResponse> mockUserResponse =
      sample.getUsers().stream().map(u -> UserResponse.of(u)).collect(Collectors.toList());

  @Before
  public void init() {
    controller = new JirikiController(mockService);
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  public void 楽曲情報が取得できる() throws Exception {
    when(mockService.getAllSongs(defaultPaging)).thenReturn(mockSongsResponse);

    mockMvc
        .perform(get(new URI("/v1/songs")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockSongsResponse)));
  }

  @Test
  public void 楽曲名で検索して楽曲情報が取得できる() throws Exception {
    when(mockService.getSongBySongName("カミサマネジマキ", defaultPaging)).thenReturn(mockSongsResponse);
    mockMvc
        .perform(get(new URI("/v1/songs?name=カミサマネジマキ")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockSongsResponse)));
  }

  @Test
  public void 投稿者名で検索して楽曲情報が取得できる() throws Exception {
    when(mockService.getSongByContributor("ミラ", defaultPaging)).thenReturn(mockSongsResponse);
    mockMvc
        .perform(get(new URI("/v1/songs?contributor=ミラ")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockSongsResponse)));
  }

  @Test
  public void 楽器名で検索して楽曲情報が取得できる() throws Exception {
    when(mockService.getSongByInstrument("ロックオルガン", defaultPaging)).thenReturn(mockSongsResponse);
    mockMvc
        .perform(get(new URI("/v1/songs?instrument=ロックオルガン")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockSongsResponse)));
  }

  @Test
  public void 地力ランクを指定して検索ができる() throws Exception {
    when(mockService.getSongByJiriki(JirikiRank.JIRIKI_E, defaultPaging))
        .thenReturn(mockSongsResponse);
    mockMvc
        .perform(get(new URI("/v1/songs?jiriki=地力Ｅ")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockSongsResponse)));
  }

  @Test
  public void 地力ランクとしてよくわからない文字列が来たら未決定で検索される() throws Exception {
    when(mockService.getSongByJiriki(JirikiRank.NON_DETERMINED, defaultPaging))
        .thenReturn(mockSongsResponse);
    mockMvc
        .perform(get(new URI("/v1/songs?jiriki=地力A")))
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
  public void 存在しない楽曲IDを指定するとスコア情報が取得できない() throws Exception {
    when(mockService.getScoresBySongId("004")).thenReturn(null);
    mockMvc.perform(get(new URI("/v1/songs/004/scores"))).andExpect(status().is4xxClientError());
  }

  @Test
  public void 全プレイヤー情報を取得できる() throws Exception {
    when(mockService.getAllPlayer()).thenReturn(mockUserResponse);
    mockMvc
        .perform(get(new URI("/v1/players")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockUserResponse)));
  }

  @Test
  public void プレイヤー名で検索できる() throws Exception {
    UserResponse user = UserResponse.of(sample.getUsers().get(0));
    List<UserResponse> users = new ArrayList<UserResponse>();
    users.add(user);

    when(mockService.getPlayerByName("妖怪1")).thenReturn(users);
    mockMvc
        .perform(get(new URI("/v1/players?name=妖怪1")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(users)));
  }

  @Test
  public void プレイヤーのtwitterIdで検索できる() throws Exception {
    UserResponse user = new UserResponse();
    user.setUserId("u001");
    user.setUserName("妖怪1");
    when(mockService.getUserSubjectFromToken()).thenReturn("twitter_id");
    when(mockService.findPlayerByTwitterId("twitter_id")).thenReturn(user);

    mockMvc
        .perform(get(new URI("/v1/players/auth0")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(user)));
  }

  @Test
  public void プレイヤーIDを指定してプレイヤー情報を取得できる() throws Exception {
    UserResponse user = UserResponse.of(sample.getUsers().get(0));
    when(mockService.getPlayerById("u001")).thenReturn(user);
    mockMvc
        .perform(get(new URI("/v1/players/u001")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(user)));
  }

  @Test
  public void twitterIdとuserIdの新規紐づけができる() throws Exception {
    TwitterUsersRequest request = new TwitterUsersRequest();
    request.setTwitterUserId("twitter_id");
    request.setUserId("u001");
    UserResponse user = new UserResponse();
    user.setUserId("u001");
    user.setUserName("妖怪1");
    when(mockService.addNewLinkBetweenUserAndTwitterUser(request)).thenReturn(user);
    mockMvc
        .perform(
            put(new URI("/v1/players"))
                .content(toJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isCreated())
        .andExpect(content().json(toJson(user)));
  }

  @Test
  public void プレイヤーIDを指定してスコア情報を取得できる() throws Exception {
    when(mockService.getScoresByUserIdWithEmpty("u001", PageRequest.of(0, 20)))
        .thenReturn(mockScore4UserResponse);
    mockMvc
        .perform(get(new URI("/v1/players/u001/scores")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockScore4UserResponse)));
  }

  @Test
  public void 存在しないプレイヤーIDを指定するとスコア情報が取得できない() throws Exception {
    when(mockService.getScoresByUserIdWithEmpty("human", PageRequest.of(0, 20))).thenReturn(null);
    mockMvc
        .perform(get(new URI("/v1/players/human/scores")))
        .andExpect(status().is4xxClientError());
  }

  private String toJson(Object object) throws JsonProcessingException {
    return objectMapper.writeValueAsString(object);
  }

  @Test
  public void プレイヤーIDと楽曲名を指定してスコア情報を取得できる() throws Exception {
    when(mockService.getScoresByUserIdAndSongNameWithEmpty(
            "u001", "みてみて☆こっちっち", PageRequest.of(0, 20)))
        .thenReturn(mockScore4UserResponse);
    mockMvc
        .perform(get(new URI("/v1/players/u001/scores?name=みてみて☆こっちっち")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockScore4UserResponse)));
  }

  @Test
  public void プレイヤーIDを投稿者名を指定してスコア情報を取得できる() throws Exception {
    when(mockService.getScoresByUserIdAndContributorWithEmpty(
            "u001", "エメラル", PageRequest.of(0, 20)))
        .thenReturn(mockScore4UserResponse);
    mockMvc
        .perform(get(new URI("/v1/players/u001/scores?contributor=エメラル")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockScore4UserResponse)));
  }

  @Test
  public void プレイヤーIDと楽器名を指定してスコア情報を取得できる() throws Exception {
    when(mockService.getScoresByUserIdAndInstrumentWithEmpty("u001", "ピアノ", PageRequest.of(0, 20)))
        .thenReturn(mockScore4UserResponse);
    mockMvc
        .perform(get(new URI("/v1/players/u001/scores?instrument=ピアノ")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockScore4UserResponse)));
  }

  @Test
  public void プレイヤーIDと地力ランクを指定してスコア情報を取得できる() throws Exception {
    when(mockService.getScoresByUserIdAndJirikiRankWithEmpty(
            "u001", JirikiRank.JIRIKI_D, PageRequest.of(0, 20)))
        .thenReturn(mockScore4UserResponse);
    mockMvc
        .perform(get(new URI("/v1/players/u001/scores?jiriki=地力Ｄ")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockScore4UserResponse)));
  }
}

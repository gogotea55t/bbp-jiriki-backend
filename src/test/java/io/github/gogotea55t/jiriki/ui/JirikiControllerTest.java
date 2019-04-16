package io.github.gogotea55t.jiriki.ui;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.gogotea55t.jiriki.domain.JirikiService;
import io.github.gogotea55t.jiriki.domain.SampleDatum;
import io.github.gogotea55t.jiriki.domain.SongsResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JirikiControllerTest {
  private MockMvc mockMvc;

  @MockBean JirikiService mockService;

  @Autowired ObjectMapper objectMapper;

  private JirikiController controller;

  private SampleDatum sample = new SampleDatum();
  List<SongsResponse> mockSongsResponse =
      sample.getSongs().stream().map(s -> SongsResponse.of(s)).collect(Collectors.toList());

  @Before
  public void init() {
    controller = new JirikiController(mockService);
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  public void 楽曲情報が取得できる() throws Exception {
    when(mockService.getAllSongs(PageRequest.of(0, 20))).thenReturn(mockSongsResponse);

    mockMvc
        .perform(get(new URI("/songs")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockSongsResponse)));
  }

  @Test
  public void 楽曲名で検索して楽曲情報が取得できる() throws Exception {
    when(mockService.getSongBySongName("カミサマネジマキ", PageRequest.of(0, 20)))
        .thenReturn(mockSongsResponse);
    mockMvc
        .perform(get(new URI("/songs?name=カミサマネジマキ")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockSongsResponse)));
  }

  @Test
  public void 投稿者名で検索して楽曲情報が取得できる() throws Exception {
    when(mockService.getSongByContributor("ミラ", PageRequest.of(0, 20)))
        .thenReturn(mockSongsResponse);
    mockMvc
        .perform(get(new URI("/songs?contributor=ミラ")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockSongsResponse)));
  }

  @Test
  public void 楽器名で検索して楽曲情報が取得できる() throws Exception {
    when(mockService.getSongByInstrument("ロックオルガン", PageRequest.of(0, 20)))
        .thenReturn(mockSongsResponse);
    mockMvc
        .perform(get(new URI("/songs?instrument=ロックオルガン")))
        .andExpect(status().isOk())
        .andExpect(content().json(toJson(mockSongsResponse)));
  }

  @Test
  public void 楽曲IDを指定して楽曲情報を取得できる() throws Exception {
    when(mockService.getSongBySongId("001")).thenReturn(SongsResponse.of(sample.getSongs().get(0)));
    mockMvc.perform(get(new URI("/songs/001"))).andExpect(status().isOk());
  }

  @Test
  public void 存在しない楽曲IDを指定して楽曲情報を取得できる() throws Exception {
    when(mockService.getSongBySongId("004")).thenReturn(null);
    mockMvc.perform(get(new URI("/songs/004"))).andExpect(status().is4xxClientError());
  }

  private String toJson(Object object) throws JsonProcessingException {
    return objectMapper.writeValueAsString(object);
  }
}

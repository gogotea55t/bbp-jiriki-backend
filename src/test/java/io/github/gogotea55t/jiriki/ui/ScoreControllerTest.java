package io.github.gogotea55t.jiriki.ui;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.gogotea55t.jiriki.AuthService;
import io.github.gogotea55t.jiriki.domain.JirikiService;
import io.github.gogotea55t.jiriki.domain.PlayerService;
import io.github.gogotea55t.jiriki.domain.ScoreService;
import io.github.gogotea55t.jiriki.domain.request.ScoreDeleteRequest;
import io.github.gogotea55t.jiriki.domain.request.ScoreRequest;
import io.github.gogotea55t.jiriki.domain.response.UserResponse;
import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;
import io.github.gogotea55t.jiriki.messaging.MessagingService;

@RunWith(SpringRunner.class)
@ActiveProfiles("unittest")
@SpringBootTest
public class ScoreControllerTest {
  private MockMvc mockMvc;
  @MockBean ScoreService mockService;
  @MockBean PlayerService playerService;
  @MockBean AuthService authService;
  @MockBean MessagingService messagingService;
  @Autowired ObjectMapper objectMapper;
  private ScoreController controller;

  @Before
  public void init() {
    controller = new ScoreController(authService, mockService, messagingService, playerService);
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  private String toJson(Object object) throws JsonProcessingException {
    return objectMapper.writeValueAsString(object);
  }

  @Test
  public void スコアの登録ができる() throws Exception {
    ScoreRequest request = new ScoreRequest();
    request.setScore(new ScoreValue(44));
    request.setUserId("u001");
    request.setSongId("001");
    when(authService.getUserSubjectFromToken()).thenReturn("token");
    UserResponse response = new UserResponse();
    response.setUserId("u001");
    when(playerService.findPlayerByTwitterId("token")).thenReturn(response);

    mockMvc
        .perform(
            put(new URI("/v1/scores"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
        .andExpect(status().isAccepted());
  }

  @Test
  public void ログインしているユーザ以外のスコアの登録はできない() throws Exception {
    ScoreRequest request = new ScoreRequest();
    request.setScore(new ScoreValue(44));
    request.setUserId("u001");
    request.setSongId("001");
    when(authService.getUserSubjectFromToken()).thenReturn("token");
    UserResponse response = new UserResponse();
    response.setUserId("u004");
    when(playerService.findPlayerByTwitterId("token")).thenReturn(response);

    mockMvc
        .perform(
            put(new URI("/v1/scores"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void スコアの削除ができる() throws Exception {
    ScoreDeleteRequest request = new ScoreDeleteRequest();
    request.setSongId("001");
    request.setUserId("u001");
    when(mockService.deleteScore(request)).thenReturn(1);
    when(authService.getUserSubjectFromToken()).thenReturn("token");
    UserResponse response = new UserResponse();
    response.setUserId("u001");
    when(playerService.findPlayerByTwitterId("token")).thenReturn(response);
    mockMvc
        .perform(
            delete(new URI("/v1/scores"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
        .andExpect(status().isNoContent());
  }

  @Test
  public void スコアの削除がされない() throws Exception {
    ScoreDeleteRequest request = new ScoreDeleteRequest();
    request.setSongId("001");
    request.setUserId("u006");
    when(mockService.deleteScore(request)).thenReturn(0);
    when(authService.getUserSubjectFromToken()).thenReturn("token");
    UserResponse response = new UserResponse();
    response.setUserId("u006");
    when(playerService.findPlayerByTwitterId("token")).thenReturn(response);
    mockMvc
        .perform(
            delete(new URI("/v1/scores"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  public void 自分以外のスコアの削除はできない() throws Exception {
    ScoreDeleteRequest request = new ScoreDeleteRequest();
    request.setSongId("001");
    request.setUserId("u006");
    when(mockService.deleteScore(request)).thenReturn(0);
    when(authService.getUserSubjectFromToken()).thenReturn("token");
    UserResponse response = new UserResponse();
    response.setUserId("u001");
    when(playerService.findPlayerByTwitterId("token")).thenReturn(response);
    mockMvc
        .perform(
            delete(new URI("/v1/scores"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
        .andExpect(status().isUnauthorized());
  }
}

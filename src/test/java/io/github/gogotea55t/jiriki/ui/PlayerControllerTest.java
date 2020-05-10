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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.gogotea55t.jiriki.AuthService;
import io.github.gogotea55t.jiriki.domain.PlayerService;
import io.github.gogotea55t.jiriki.domain.SampleDatum;
import io.github.gogotea55t.jiriki.domain.request.TwitterUsersRequest;
import io.github.gogotea55t.jiriki.domain.response.UserResponse;

@RunWith(SpringRunner.class)
@ActiveProfiles("unittest")
@SpringBootTest
public class PlayerControllerTest {
  private MockMvc mockMvc;
  @MockBean PlayerService mockService;
  @MockBean AuthService authService;
  @Autowired ObjectMapper objectMapper;
  @Autowired ExceptionHandlerAdvice exceptionHandler;
  private PlayerController controller;

  SampleDatum sample = new SampleDatum();
  List<UserResponse> mockUserResponse =
	      sample.getUsers().stream().map(u -> UserResponse.of(u)).collect(Collectors.toList());

  @Before
  public void init() {
    controller = new PlayerController(mockService, authService);
    mockMvc = MockMvcBuilders.standaloneSetup(controller, exceptionHandler).build();
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
    when(authService.getUserSubjectFromToken()).thenReturn("twitter_id");
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
    when(authService.getUserSubjectFromToken()).thenReturn("twitter_id");
    request.setTwitterUserId("twitter_id");
    request.setUserId("u001");
    UserResponse user = new UserResponse();
    user.setUserId("u001");
    user.setUserName("妖怪1");
    when(mockService.addNewLinkBetweenUserAndTwitterUser(request)).thenReturn(user);
    mockMvc
        .perform(
            put(new URI("/v1/players/auth0"))
                .content(toJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isCreated())
        .andExpect(content().json(toJson(user)));
  }

  @Test
  public void twitterIdとuserIdの紐づけをしようとしたらトークンがおかしい() throws Exception {
    TwitterUsersRequest request = new TwitterUsersRequest();
    when(authService.getUserSubjectFromToken()).thenThrow(new JWTDecodeException(""));
    request.setTwitterUserId("twitter_id");
    request.setUserId("u001");
    UserResponse user = new UserResponse();
    user.setUserId("u001");
    user.setUserName("妖怪1");
    when(mockService.addNewLinkBetweenUserAndTwitterUser(request)).thenReturn(user);
    mockMvc
        .perform(
            put(new URI("/v1/players/auth0"))
                .content(toJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void twitterIdとuserIdの紐づけをするが他人名義のものをやろうとする() throws Exception {
    TwitterUsersRequest request = new TwitterUsersRequest();
    when(authService.getUserSubjectFromToken()).thenReturn("twitter_id");
    request.setTwitterUserId("others_twitter_id");
    request.setUserId("u004");
    mockMvc
        .perform(
            put(new URI("/v1/players/auth0"))
                .content(toJson(request))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().is4xxClientError());
  }
  
  private String toJson(Object object) throws JsonProcessingException {
	    return objectMapper.writeValueAsString(object);
	  }
}

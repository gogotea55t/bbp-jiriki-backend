package io.github.gogotea55t.jiriki.ui;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.ArrayList;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.gogotea55t.jiriki.domain.JirikiService;
import io.github.gogotea55t.jiriki.domain.SampleDatum;
import io.github.gogotea55t.jiriki.domain.request.PageRequest;
import io.github.gogotea55t.jiriki.domain.request.ScoreDeleteRequest;
import io.github.gogotea55t.jiriki.domain.request.ScoreRequest;
import io.github.gogotea55t.jiriki.domain.request.TwitterUsersRequest;
import io.github.gogotea55t.jiriki.domain.response.Score4SongResponse;
import io.github.gogotea55t.jiriki.domain.response.Score4SongResponseV2;
import io.github.gogotea55t.jiriki.domain.response.Score4UserResponse;
import io.github.gogotea55t.jiriki.domain.response.Score4UserResponseV2;
import io.github.gogotea55t.jiriki.domain.response.SongsResponse;
import io.github.gogotea55t.jiriki.domain.response.UserResponse;
import io.github.gogotea55t.jiriki.domain.vo.JirikiRank;
import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;

@RunWith(SpringRunner.class)
@ActiveProfiles("unittest")
@SpringBootTest
public class JirikiControllerTest {
  private MockMvc mockMvc;

  @MockBean JirikiService mockService;

  @Autowired ObjectMapper objectMapper;

  @Autowired ExceptionHandlerAdvice exceptionHandler;

  private JirikiController controller;

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
    controller = new JirikiController(mockService);
    query = new HashMap<String, String>();
    mockMvc = MockMvcBuilders.standaloneSetup(controller, exceptionHandler).build();
  }

  @Test
  public void test() {}


}

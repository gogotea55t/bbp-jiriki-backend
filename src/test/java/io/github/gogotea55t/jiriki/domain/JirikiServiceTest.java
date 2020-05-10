package io.github.gogotea55t.jiriki.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import io.github.gogotea55t.jiriki.AuthService;
import io.github.gogotea55t.jiriki.domain.entity.Scores;
import io.github.gogotea55t.jiriki.domain.entity.TwitterUsers;
import io.github.gogotea55t.jiriki.domain.factory.ScoresFactory;
import io.github.gogotea55t.jiriki.domain.repository.ScoresRepository;
import io.github.gogotea55t.jiriki.domain.repository.SongRepository;
import io.github.gogotea55t.jiriki.domain.repository.TwitterUsersRepository;
import io.github.gogotea55t.jiriki.domain.repository.UserRepository;
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
import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;

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

  @Autowired private ScoresFactory scoreFactory;

  @MockBean private RabbitTemplate rabbitTemplate;

  @MockBean private AuthService mockAuthService;

  @Rule public ExpectedException expectedException = ExpectedException.none();

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
            twiRepository,
            scoreFactory,
            rabbitTemplate,
            mockAuthService);
    SampleDatum sample = new SampleDatum();
    userRepository.deleteAll();
    songRepository.deleteAll();
    scoreRepository.deleteAll();
    twiRepository.deleteAll();

    userRepository.saveAll(sample.getUsers());
    songRepository.saveAll(sample.getSongs());
    scoreRepository.saveAll(sample.getScores());
    twiRepository.save(new TwitterUsers("twitterId", sample.getUsers().get(1)));
    query = new HashMap<String, String>();
  }
  
  @Test
  public void test() {}


}

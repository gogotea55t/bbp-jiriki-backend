package io.github.gogotea55t.jiriki.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.gogotea55t.jiriki.domain.request.ScoreDeleteRequest;
import io.github.gogotea55t.jiriki.domain.request.ScoreRequest;
import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;
import io.github.gogotea55t.jiriki.domain.vo.user.UserId;
import io.github.gogotea55t.jiriki.messaging.MessagingService;

@RunWith(SpringRunner.class)
@ActiveProfiles("unittest")
@SpringBootTest
public class MessagingServiceTest {
  private MessagingService messagingService;
  @MockBean private RabbitTemplate rabbitTemplate;
  
  @Before
  public void init() {
	messagingService = new MessagingService(rabbitTemplate);
  }
  
  @Test
  public void 登録メッセージを送信してもエラーが出ない() {
	ScoreRequest request = new ScoreRequest();
	request.setScore(new ScoreValue("34.44"));
	request.setSongId("555");
	request.setUserId(new UserId("u999"));
	messagingService.messagingTest(request);
  }
  
  @Test
  public void 削除メッセージを送信してもエラーが出ない() {
	ScoreDeleteRequest request = new ScoreDeleteRequest();
	request.setSongId("555");
	request.setUserId(new UserId("u999"));
	messagingService.deleteRequest(request);
  }
}

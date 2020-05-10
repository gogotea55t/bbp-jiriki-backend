package io.github.gogotea55t.jiriki.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.gogotea55t.jiriki.domain.request.ScoreDeleteRequest;
import io.github.gogotea55t.jiriki.domain.request.ScoreRequest;

@Service
public class MessagingService {
  private RabbitTemplate rabbitTemplate;

  @Autowired
  public MessagingService(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  /**
   * スプレッドシートからスコア情報を登録する支持をRabbitMQに投げる
   * @param request
   */
  public void messagingTest(ScoreRequest request) {
    rabbitTemplate.convertAndSend("jiriki-bbp-spreadsheet", "update", request);
  }

  /**
   * スプレッドシートからスコア情報を削除する指示をRabbitMQに投げる
   * @param request
   */
  public void deleteRequest(ScoreDeleteRequest request) {
    rabbitTemplate.convertAndSend("jiriki-bbp-spreadsheet", "delete", request);
  }
}

package io.github.gogotea55t.jiriki.messaging;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MessagingConfiguration {

  String topicName = "jiriki-bbp-spreadsheet";

  @Bean
  TopicExchange topic() {
    return new TopicExchange(topicName, false, true);
  }

  @Bean
  Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}

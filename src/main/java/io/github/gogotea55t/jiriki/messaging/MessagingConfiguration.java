package io.github.gogotea55t.jiriki.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MessagingConfiguration {
	
  String queueName = "jiriki-bbp-spreadsheet";
  
  @Bean
  Queue queue() {
	return new Queue(queueName, false);
  }
  
  @Bean
  Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
	return new Jackson2JsonMessageConverter();
  }
  
}

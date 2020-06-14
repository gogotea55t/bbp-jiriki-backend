package io.github.gogotea55t.jiriki.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import io.github.gogotea55t.jiriki.domain.vo.GenericStringValueHandler;
import io.github.gogotea55t.jiriki.domain.vo.user.UserId;
import io.github.gogotea55t.jiriki.domain.vo.user.UserName;

@Component
public class MyBatisConfiguration {
  @Bean
  public GenericStringValueHandler<UserId> userIdHandler() {
    return new GenericStringValueHandler<>(UserId.class);
  }

  @Bean
  public GenericStringValueHandler<UserName> userNameHandler() {
    return new GenericStringValueHandler<>(UserName.class);
  }
}

package io.github.gogotea55t.jiriki.domain.entity;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TwitterUsers {
  private String twitterUserId;

  private Users users;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  public TwitterUsers(String twitterUserId, Users users) {
    this.twitterUserId = twitterUserId;
    this.users = users;
  }
}

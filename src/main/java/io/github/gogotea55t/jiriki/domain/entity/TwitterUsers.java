package io.github.gogotea55t.jiriki.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TwitterUsers {
  private String twitterUserId;

  private Users users;
}

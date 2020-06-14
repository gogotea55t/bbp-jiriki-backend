package io.github.gogotea55t.jiriki.domain.request;

import io.github.gogotea55t.jiriki.domain.vo.user.UserId;
import lombok.Data;

@Data
public class TwitterUsersRequest {
  private UserId userId;
  private String twitterUserId;
}

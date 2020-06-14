package io.github.gogotea55t.jiriki.domain.response;

import java.util.List;

import io.github.gogotea55t.jiriki.domain.vo.user.UserId;
import lombok.Data;

@Data
public class TwitterUserResponse {
  private UserId userId;
  
  private List<String> twitterUserId;
}

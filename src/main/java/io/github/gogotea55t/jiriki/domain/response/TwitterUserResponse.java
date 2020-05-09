package io.github.gogotea55t.jiriki.domain.response;

import java.util.List;

import lombok.Data;

@Data
public class TwitterUserResponse {
  private String userId;
  
  private List<String> twitterUserId;
}

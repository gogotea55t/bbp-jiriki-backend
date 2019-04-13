package io.github.gogotea55t.jiriki.domain;

import io.github.gogotea55t.jiriki.domain.entity.Users;
import lombok.Data;

@Data
public class UserResponse {
  private String userName;

  public static UserResponse of(Users user) {
    UserResponse response = new UserResponse();
    response.setUserName(user.getUserName());

    return response;
  }
}

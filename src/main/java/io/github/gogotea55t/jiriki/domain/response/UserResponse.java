package io.github.gogotea55t.jiriki.domain.response;

import io.github.gogotea55t.jiriki.domain.entity.Users;
import io.github.gogotea55t.jiriki.domain.vo.user.UserId;
import io.github.gogotea55t.jiriki.domain.vo.user.UserName;
import lombok.Data;

@Data
public class UserResponse {
  private UserId userId;
  private UserName userName;

  public static UserResponse of(Users user) {
    UserResponse response = new UserResponse();
    response.setUserName(user.getUserName());
    response.setUserId(user.getUserId());

    return response;
  }
}

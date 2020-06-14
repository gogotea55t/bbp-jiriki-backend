package io.github.gogotea55t.jiriki.domain.vo.user;

import com.fasterxml.jackson.annotation.JsonValue;

import io.github.gogotea55t.jiriki.domain.vo.StringValueObject;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class UserName extends StringValueObject {
  private static final int MAX_LENGTH = 15;
  @JsonValue private String value;

  public UserName(String userName) {
    super(userName);
    if (userName == null) {
      throw new IllegalArgumentException("ユーザ名の入力は必須です。");
    }
    if (userName.length() > MAX_LENGTH) {
      throw new IllegalArgumentException("ユーザ名は" + MAX_LENGTH + "文字以上にできません。");
    }
    this.value = userName;
  }
}

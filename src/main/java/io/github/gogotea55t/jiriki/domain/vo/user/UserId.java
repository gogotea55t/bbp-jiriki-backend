package io.github.gogotea55t.jiriki.domain.vo.user;

import com.fasterxml.jackson.annotation.JsonValue;

import io.github.gogotea55t.jiriki.domain.vo.StringValueObject;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class UserId extends StringValueObject {
  private static final int MAX_LENGTH = 10;
  @JsonValue private String value;

  public UserId(String userId) {
	super(userId);
    if (userId == null) {
      throw new IllegalArgumentException("ユーザIDの入力は必須です。");
    }
    if (userId.length() > MAX_LENGTH) {
      throw new IllegalArgumentException("ユーザ名は" + MAX_LENGTH + "文字以上にできません。");
    }
    this.value = userId;
  }
}

package io.github.gogotea55t.jiriki.domain.vo.song;

import com.fasterxml.jackson.annotation.JsonValue;

import io.github.gogotea55t.jiriki.domain.vo.StringValueObject;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class Contributor extends StringValueObject {
  @JsonValue private final String value;
  private final int MAX_LENGTH = 20;

  public Contributor(String value) {
    super(value);
    if (value == null) {
      throw new IllegalArgumentException("投稿者名の入力は必須です。");
    }
    if (value.length() > MAX_LENGTH) {
      throw new IllegalArgumentException("投稿者名は" + MAX_LENGTH + "文字以上にできません。");
    }
    this.value = value;
  }
}

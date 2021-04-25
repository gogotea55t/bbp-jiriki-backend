package io.github.gogotea55t.jiriki.domain.vo.song;

import com.fasterxml.jackson.annotation.JsonValue;

import io.github.gogotea55t.jiriki.domain.vo.StringValueObject;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class Instrument extends StringValueObject {
  @JsonValue private final String value;
  private final int MAX_LENGTH = 15;

  public Instrument(String value) {
    super(value);
    if (value == null) {
      throw new IllegalArgumentException("楽器名の入力は必須です。");
    }
    if (value.length() > MAX_LENGTH) {
      throw new IllegalArgumentException("楽器名は" + MAX_LENGTH + "文字以上にできません。");
    }
    this.value = value;
  }
}

package io.github.gogotea55t.jiriki.domain.vo.song;

import com.fasterxml.jackson.annotation.JsonValue;

import io.github.gogotea55t.jiriki.domain.vo.StringValueObject;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class SongName extends StringValueObject {
  @JsonValue private final String value;
  private final int MAX_LENGTH = 60;

  public SongName(String value) {
    super(value);
    if (value == null) {
      throw new IllegalArgumentException("楽曲名の入力は必須です。");
    }
    if (value.length() > MAX_LENGTH) {
      throw new IllegalArgumentException("楽曲名は" + MAX_LENGTH + "文字以上にできません。");
    }
    this.value = value;
  }
}

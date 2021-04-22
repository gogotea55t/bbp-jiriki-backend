package io.github.gogotea55t.jiriki.domain.vo.song;

import com.fasterxml.jackson.annotation.JsonValue;

import io.github.gogotea55t.jiriki.domain.vo.StringValueObject;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class SongId extends StringValueObject {
  @JsonValue private final String value;
  private final int MAX_LENGTH = 5;

  public SongId(String songId) {
    super(songId);
    if (songId == null) {
      throw new IllegalArgumentException("楽曲IDの入力は必須です。");
    }
    if (songId.length() > MAX_LENGTH) {
      throw new IllegalArgumentException("楽曲IDは" + MAX_LENGTH + "以上にできません。");
    }
    this.value = songId;
  }
}

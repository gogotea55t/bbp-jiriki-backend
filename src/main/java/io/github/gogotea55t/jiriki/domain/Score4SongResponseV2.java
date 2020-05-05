package io.github.gogotea55t.jiriki.domain;

import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;
import lombok.Data;

@Data
public class Score4SongResponseV2 {
  private String userId;
  private String userName;
  private ScoreValue score;
}

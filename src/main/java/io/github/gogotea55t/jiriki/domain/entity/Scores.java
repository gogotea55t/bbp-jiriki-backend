package io.github.gogotea55t.jiriki.domain.entity;

import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;
import lombok.Data;

@Data
public class Scores {
  private long scoreId;

  private Users users;

  private Songs songs;

  private ScoreValue score;
}

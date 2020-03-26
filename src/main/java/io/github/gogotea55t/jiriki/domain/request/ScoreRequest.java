package io.github.gogotea55t.jiriki.domain.request;

import java.io.Serializable;

import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;
import lombok.Data;

@Data
public class ScoreRequest implements Serializable {
  private static final long serialVersionUID = 1L;

  private String userId;

  private String songId;

  private ScoreValue score;
}

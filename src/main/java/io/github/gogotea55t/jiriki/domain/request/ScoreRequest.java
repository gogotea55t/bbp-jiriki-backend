package io.github.gogotea55t.jiriki.domain.request;

import java.io.Serializable;

import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;
import io.github.gogotea55t.jiriki.domain.vo.user.UserId;
import lombok.Data;

@Data
public class ScoreRequest implements Serializable {
  private static final long serialVersionUID = 1L;

  private UserId userId;

  private String songId;

  private ScoreValue score;
}

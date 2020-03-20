package io.github.gogotea55t.jiriki.domain.request;

import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;
import lombok.Data;

@Data
public class ScoreRequest {
  private String userId;
  
  private String songId;
  
  private ScoreValue score;
}

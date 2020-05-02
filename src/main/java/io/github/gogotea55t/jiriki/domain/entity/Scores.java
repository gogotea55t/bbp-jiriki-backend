package io.github.gogotea55t.jiriki.domain.entity;

import java.time.LocalDateTime;

import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;
import lombok.Data;

@Data
public class Scores {
  private long scoreId;

  private Users users;

  private Songs songs;

  private ScoreValue score;
  
  private LocalDateTime createdAt;
  
  private String createdBy;
  
  private LocalDateTime updatedAt;
  
  private String updatedBy;
}

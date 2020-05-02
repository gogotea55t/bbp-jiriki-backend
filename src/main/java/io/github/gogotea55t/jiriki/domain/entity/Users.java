package io.github.gogotea55t.jiriki.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class Users {
  /** ユーザー識別ID */
  private String userId;

  /** ユーザー名 */
  private String userName;

  /** スコア */
  private List<Scores> scores;
  
  private LocalDateTime createdAt;
  
  private LocalDateTime updatedAt;
}

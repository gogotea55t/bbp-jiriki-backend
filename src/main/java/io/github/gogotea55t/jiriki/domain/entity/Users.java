package io.github.gogotea55t.jiriki.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

import io.github.gogotea55t.jiriki.domain.vo.user.UserId;
import io.github.gogotea55t.jiriki.domain.vo.user.UserName;
import lombok.Data;

@Data
public class Users {
  /** ユーザー識別ID */
  private UserId userId;

  /** ユーザー名 */
  private UserName userName;

  /** スコア */
  private List<Scores> scores;
  
  private LocalDateTime createdAt;
  
  private LocalDateTime updatedAt;
}

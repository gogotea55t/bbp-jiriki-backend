package io.github.gogotea55t.jiriki.domain.response;

import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;
import io.github.gogotea55t.jiriki.domain.vo.user.UserId;
import io.github.gogotea55t.jiriki.domain.vo.user.UserName;
import lombok.Data;

@Data
public class Score4SongResponseV2 {
  private UserId userId;
  private UserName userName;
  private ScoreValue score;
}

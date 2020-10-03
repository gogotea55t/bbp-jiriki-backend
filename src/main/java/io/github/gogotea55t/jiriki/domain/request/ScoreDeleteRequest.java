package io.github.gogotea55t.jiriki.domain.request;

import java.io.Serializable;

import io.github.gogotea55t.jiriki.domain.vo.song.SongId;
import io.github.gogotea55t.jiriki.domain.vo.user.UserId;
import lombok.Data;

@Data
public class ScoreDeleteRequest implements Serializable {
  private static final long serialVersionUID = 1L;

  private UserId userId;

  private SongId songId;
}

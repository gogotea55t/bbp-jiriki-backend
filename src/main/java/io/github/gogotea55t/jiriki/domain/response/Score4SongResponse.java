package io.github.gogotea55t.jiriki.domain.response;

import io.github.gogotea55t.jiriki.domain.entity.Scores;
import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;
import lombok.Data;

@Data
public class Score4SongResponse {
  private String userName;

  private ScoreValue score;

  public static Score4SongResponse of(Scores scores) {
    Score4SongResponse response = new Score4SongResponse();
    response.setScore(scores.getScore());
    response.setUserName(scores.getUsers().getUserName());

    return response;
  }
}

package io.github.gogotea55t.jiriki.domain;

import io.github.gogotea55t.jiriki.domain.vo.JirikiRank;
import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;
import lombok.Data;

@Data
public class Score4UserResponseV2 {
  private String songId;

  private JirikiRank jirikiRank;

  private String songName;

  private String contributor;

  private String instrument;

  private ScoreValue score;
  
  private ScoreValue average;
  
  private ScoreValue max;
}

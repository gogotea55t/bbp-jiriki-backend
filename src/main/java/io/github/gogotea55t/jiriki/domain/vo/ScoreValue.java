package io.github.gogotea55t.jiriki.domain.vo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Data;

@Data
public class ScoreValue {
  @JsonValue private final BigDecimal score;

  public ScoreValue(Integer score) {
    if (score == null) {
      // TODO: 本当にこれでいいか？
      this.score = null;
    } else if (score < 0 || score > 100) {
      throw new IllegalArgumentException("得点は0から100までの入力が必要です");
    } else {
      this.score = new BigDecimal(score);
    }
  }

  public ScoreValue(Double score) {
    if (score < 0 || score > 100) {
      throw new IllegalArgumentException("得点は0から100までの入力が必要です");
    }
    this.score = new BigDecimal(score);
    this.score.setScale(4, 0);
  }
}

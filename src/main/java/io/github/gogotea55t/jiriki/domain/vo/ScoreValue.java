package io.github.gogotea55t.jiriki.domain.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Data;

@Data
public class ScoreValue {
  @JsonValue private final BigDecimal score;

  public ScoreValue(Integer score) {
    if (score == null) {
      // TODO: 本当にこれでいいか？
      this.score = null;
    } else if (isInvalidRange(score)) {
      throw new IllegalArgumentException("得点は0から100までの入力が必要です");
    } else {
      this.score = new BigDecimal(score);
    }
  }

  public ScoreValue(Double score) {
    if (score == null) {
      this.score = null;
    } else if (isInvalidRange(score)) {
      throw new IllegalArgumentException("得点は0から100までの入力が必要です");
    } else {
      this.score = BigDecimal.valueOf(score).setScale(2, RoundingMode.HALF_UP);
    }
  }

  public ScoreValue(BigDecimal score) {
    if (score == null) {
      this.score = null;
    } else if (isInvalidRange(score)) {
      throw new IllegalArgumentException("得点は0から100までの入力が必要です");
    } else {
      this.score = score.setScale(2, RoundingMode.HALF_UP);
    }
  }

  public ScoreValue(String score) {
    try {
      if (score == null) {
        this.score = null;
      } else {
        BigDecimal sc = new BigDecimal(score).setScale(2);
        if (isInvalidRange(sc)) {
          throw new IllegalArgumentException("得点は0から100までの入力が必要です");
        } else {
          this.score = sc;
        }
      }
    } catch (NumberFormatException ne) {
      throw new IllegalArgumentException("半角数字のみ入力可能です。");
    }
  }

  private boolean isInvalidRange(Number score) {
    return score.doubleValue() < 0 || score.doubleValue() > 100;
  }

  public boolean isInsertableToDB() {
    return (score != null) && !(score.toPlainString().contains("."));
  }

  public boolean isEqualTo(ScoreValue sc) {
    return this.score.compareTo(sc.getScore()) == 0;
  }
}

package io.github.gogotea55t.jiriki.domain.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

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

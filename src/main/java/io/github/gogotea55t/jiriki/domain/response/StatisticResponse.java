package io.github.gogotea55t.jiriki.domain.response;

import lombok.Data;

@Data
public class StatisticResponse {
  private int gold;
  private int silver;
  private int bronze;
  private int blue;
  private int gray;
  private int none;
}

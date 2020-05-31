package io.github.gogotea55t.jiriki.domain.response;

import io.github.gogotea55t.jiriki.domain.vo.JirikiRank;
import lombok.Data;

@Data
public class StatisticsResponseByJirikiRank {
  private JirikiRank jirikiRank;
  private StatisticResponse stats;
}

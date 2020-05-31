package io.github.gogotea55t.jiriki.domain.response;

import java.util.List;

import lombok.Data;

@Data
public class StatisticsResponseDetail {
  private List<StatisticsResponseByJirikiRank> detail;

  public StatisticsResponseDetail(List<StatisticsResponseByJirikiRank> detail) {
    this.detail = detail;
  }
}

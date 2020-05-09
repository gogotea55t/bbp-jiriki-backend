package io.github.gogotea55t.jiriki.domain.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class WeeklyChallange {
  private int weeklyChallangeId;
  private LocalDate startDate;
  private LocalDate endDate;
  private Songs songs;
}

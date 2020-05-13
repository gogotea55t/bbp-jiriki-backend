package io.github.gogotea55t.jiriki.domain.response;

import java.time.LocalDate;

import io.github.gogotea55t.jiriki.domain.entity.WeeklyChallange;
import lombok.Data;

@Data
public class WeeklyChallangeResponse {
  private int weeklyChallangeId;
  private SongsResponse song;
  private LocalDate start;
  private LocalDate end;

  public static WeeklyChallangeResponse of(WeeklyChallange wc) {
    WeeklyChallangeResponse response = new WeeklyChallangeResponse();
    response.setWeeklyChallangeId(wc.getWeeklyChallangeId());
    response.setSong(SongsResponse.of(wc.getSongs()));
    response.setStart(wc.getStartDate());
    response.setEnd(wc.getEndDate());
    return response;
  }
}

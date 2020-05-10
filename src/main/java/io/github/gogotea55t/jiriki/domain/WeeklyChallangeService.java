package io.github.gogotea55t.jiriki.domain;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import io.github.gogotea55t.jiriki.domain.entity.WeeklyChallange;
import io.github.gogotea55t.jiriki.domain.repository.WeeklyChallangeRepository;
import io.github.gogotea55t.jiriki.domain.response.WeeklyChallangeResponse;

@Service
public class WeeklyChallangeService {
  private SongService songService;
  private WeeklyChallangeRepository weeklyChallangeRepository;
  @Autowired
  public WeeklyChallangeService(SongService songService, WeeklyChallangeRepository weeklyChallangeRepository) {
	this.songService = songService;
	this.weeklyChallangeRepository = weeklyChallangeRepository;
  }
  
  @Scheduled(cron = "0 0 0 * * 1")
  public void setWeeklyChallange() {
    LocalDate startDate = LocalDate.now();
    LocalDate endDate = startDate.plusDays(6);
    WeeklyChallange wc = new WeeklyChallange();
    wc.setSongs(songService.getSongEntityByRandom());
    wc.setStartDate(startDate);
    wc.setEndDate(endDate);
    weeklyChallangeRepository.save(wc);
  }
  
  public WeeklyChallangeResponse getLatestWeeklyChallange() {
	WeeklyChallange wc = weeklyChallangeRepository.findLatest();
	return WeeklyChallangeResponse.of(wc);
  }
}
package io.github.gogotea55t.jiriki.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import io.github.gogotea55t.jiriki.domain.WeeklyChallangeService;
import io.github.gogotea55t.jiriki.domain.response.WeeklyChallangeResponse;

@Controller
public class WeeklyChallangeController {
  private WeeklyChallangeService wcService;

  public WeeklyChallangeController(WeeklyChallangeService wcService) {
    this.wcService = wcService;
  }
  
  @PostMapping("/v1/weekly_challange")
  public ResponseEntity<?> addNewSong() {
	wcService.setWeeklyChallange();
	return ResponseEntity.ok().build();
  }
  
  @GetMapping("/v1/weekly_challange")
  public ResponseEntity<?> weeklyChallange() {
	WeeklyChallangeResponse response = wcService.getLatestWeeklyChallange();
	return ResponseEntity.ok(response);
  }
}

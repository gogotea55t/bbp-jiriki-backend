package io.github.gogotea55t.jiriki.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.github.gogotea55t.jiriki.AuthService;
import io.github.gogotea55t.jiriki.domain.JirikiService;
import io.github.gogotea55t.jiriki.domain.PlayerService;
import io.github.gogotea55t.jiriki.domain.ScoreService;
import io.github.gogotea55t.jiriki.domain.request.ScoreDeleteRequest;
import io.github.gogotea55t.jiriki.domain.request.ScoreRequest;
import io.github.gogotea55t.jiriki.messaging.MessagingService;

@Controller
public class ScoreController {
  private AuthService authService;
  private ScoreService scoreService;
  private PlayerService playerService;
  private MessagingService messagingService;

  @Autowired
  public ScoreController(
      AuthService authService, ScoreService scoreService, MessagingService messagingService, PlayerService playerService) {
    this.authService = authService;
    this.scoreService = scoreService;
    this.messagingService = messagingService;
    this.playerService = playerService;
  }

  @PutMapping("/v1/scores")
  public ResponseEntity<?> registerScore(@RequestBody ScoreRequest request) {
    if (!request.getUserId().equals(loginUserId())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    scoreService.registerScore(request);
    messagingService.messagingTest(request);
    return ResponseEntity.accepted().build();
  }

  @DeleteMapping("/v1/scores")
  public ResponseEntity<?> deleteScore(@RequestBody ScoreDeleteRequest request) {
    if (!request.getUserId().equals(loginUserId())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    int deleted = scoreService.deleteScore(request);

    if (deleted == 1) {
      // 実際に削除した時はスプレッドシートからも消そうとする
      messagingService.deleteRequest(request);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  private String loginUserId() {
    String auth0UserId = authService.getUserSubjectFromToken();
    return playerService.findPlayerByTwitterId(auth0UserId).getUserId();
  }
}

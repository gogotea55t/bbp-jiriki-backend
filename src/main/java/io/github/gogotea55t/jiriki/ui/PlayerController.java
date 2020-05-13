package io.github.gogotea55t.jiriki.ui;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.gogotea55t.jiriki.AuthService;
import io.github.gogotea55t.jiriki.domain.PlayerService;
import io.github.gogotea55t.jiriki.domain.request.TwitterUsersRequest;
import io.github.gogotea55t.jiriki.domain.response.ErrorResponse;
import io.github.gogotea55t.jiriki.domain.response.UserResponse;

@Controller
public class PlayerController {

  private PlayerService playerService;
  private AuthService authService;

  @Autowired
  public PlayerController(PlayerService playerService, AuthService authService) {
    this.playerService = playerService;
    this.authService = authService;
  }
  
  @GetMapping("/v1/players")
  public ResponseEntity<?> getPlayer(@RequestParam(required = false) String name) {
    if (name != null) {
      List<UserResponse> result = playerService.getPlayerByName(name);
      return ResponseEntity.ok(result);
    } else {
      List<UserResponse> result = playerService.getAllPlayer();
      return ResponseEntity.ok().body(result);
    }
  }

  @GetMapping("/v1/players/auth0")
  public ResponseEntity<?> getPlayerFromAuth0() {
    String auth0Id = authService.getUserSubjectFromToken();
    UserResponse result = playerService.findPlayerByTwitterId(auth0Id);
    return ResponseEntity.ok(result);
  }

  @PutMapping("/v1/players/auth0")
  public ResponseEntity<?> addNewLinkBetweenUserAndTwitterUser(
      @RequestBody TwitterUsersRequest request) {
    String auth0Id = authService.getUserSubjectFromToken();
    if (auth0Id.equals(request.getTwitterUserId())) {
      UserResponse response = playerService.addNewLinkBetweenUserAndTwitterUser(request);
      return ResponseEntity.created(URI.create("/v1/players/" + response.getUserId()))
          .body(response);
    } else {
      ErrorResponse error = new ErrorResponse();
      error.setMessage("ログインしているアカウントの連携情報しか管理できません。");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
  }

  @GetMapping("/v1/players/{id}")
  public ResponseEntity<?> getPlayerById(@PathVariable(name = "id") String id) {
    UserResponse response = playerService.getPlayerById(id);
    if (response == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(response);
    }
  }
}

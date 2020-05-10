package io.github.gogotea55t.jiriki.ui;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.auth0.spring.security.api.JwtAuthenticationProvider;

import io.github.gogotea55t.jiriki.domain.JirikiService;
import io.github.gogotea55t.jiriki.domain.request.PageRequest;
import io.github.gogotea55t.jiriki.domain.request.ScoreDeleteRequest;
import io.github.gogotea55t.jiriki.domain.request.ScoreRequest;
import io.github.gogotea55t.jiriki.domain.request.TwitterUsersRequest;
import io.github.gogotea55t.jiriki.domain.response.ErrorResponse;
import io.github.gogotea55t.jiriki.domain.response.Score4SongResponse;
import io.github.gogotea55t.jiriki.domain.response.Score4SongResponseV2;
import io.github.gogotea55t.jiriki.domain.response.Score4UserResponse;
import io.github.gogotea55t.jiriki.domain.response.Score4UserResponseV2;
import io.github.gogotea55t.jiriki.domain.response.SongsResponse;
import io.github.gogotea55t.jiriki.domain.response.UserResponse;

@Controller
public class JirikiController {

  JirikiService jirikiService;
  JwtAuthenticationProvider provider;

  @Autowired
  public JirikiController(JirikiService jirikiService) {
    this.jirikiService = jirikiService;
  }

  /** @return */
  @GetMapping("/spreadsheet")
  public ResponseEntity<?> get() {
    jirikiService.doGet();
    return ResponseEntity.ok().body("ok");
  }



  @GetMapping("/v1/jiriki")
  public ResponseEntity<?> getSongByJiriki() {
    return ResponseEntity.ok().build();
  }
}

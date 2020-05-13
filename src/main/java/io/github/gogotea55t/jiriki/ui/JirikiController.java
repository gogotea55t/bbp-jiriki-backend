package io.github.gogotea55t.jiriki.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.auth0.spring.security.api.JwtAuthenticationProvider;

import io.github.gogotea55t.jiriki.domain.JirikiService;

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

package io.github.gogotea55t.jiriki.ui;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.auth0.spring.security.api.JwtAuthenticationProvider;

import io.github.gogotea55t.jiriki.domain.ErrorResponse;
import io.github.gogotea55t.jiriki.domain.JirikiService;
import io.github.gogotea55t.jiriki.domain.Score4SongResponse;
import io.github.gogotea55t.jiriki.domain.Score4SongResponseV2;
import io.github.gogotea55t.jiriki.domain.Score4UserResponse;
import io.github.gogotea55t.jiriki.domain.SongsResponse;
import io.github.gogotea55t.jiriki.domain.UserResponse;
import io.github.gogotea55t.jiriki.domain.request.PageRequest;
import io.github.gogotea55t.jiriki.domain.request.ScoreRequest;
import io.github.gogotea55t.jiriki.domain.request.TwitterUsersRequest;

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

  @GetMapping("/v1/songs")
  public ResponseEntity<?> getSong(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String contributor,
      @RequestParam(required = false) String instrument,
      @RequestParam(required = false) String jiriki,
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "20") Integer limit) {
    PageRequest pageReq = new PageRequest(page, limit);
    Map<String, String> query = new HashMap<String, String>();
    if (name != null) {
      query.put("name", name);
    } else if (contributor != null) {
      query.put("contributor", contributor);
    } else if (instrument != null) {
      query.put("instrument", instrument);
    } else if (jiriki != null) {
      query.put("jiriki", jiriki);
    }
    Object result = jirikiService.searchSongsByQuery(query, pageReq);
    System.out.println(result);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/v1/players")
  public ResponseEntity<?> getPlayer(@RequestParam(required = false) String name) {
    if (name != null) {
      List<UserResponse> result = jirikiService.getPlayerByName(name);
      return ResponseEntity.ok(result);
    } else {
      List<UserResponse> result = jirikiService.getAllPlayer();
      return ResponseEntity.ok().body(result);
    }
  }

  @GetMapping("/v1/players/auth0")
  public ResponseEntity<?> getPlayerFromAuth0() {
    String auth0Id = jirikiService.getUserSubjectFromToken();
    UserResponse result = jirikiService.findPlayerByTwitterId(auth0Id);
    return ResponseEntity.ok(result);
  }

  @PutMapping("/v1/players/auth0")
  public ResponseEntity<?> addNewLinkBetweenUserAndTwitterUser(
      @RequestBody TwitterUsersRequest request) {
    String auth0Id = jirikiService.getUserSubjectFromToken();
    if (auth0Id.equals(request.getTwitterUserId())) {
      UserResponse response = jirikiService.addNewLinkBetweenUserAndTwitterUser(request);
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
    UserResponse response = jirikiService.getPlayerById(id);
    if (response == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(response);
    }
  }

  @GetMapping("/v1/players/average/scores")
  public ResponseEntity<?> getAverage(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String contributor,
      @RequestParam(required = false) String instrument,
      @RequestParam(required = false) String jiriki,
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "20") Integer limit) {
    PageRequest pageReq = new PageRequest(page * limit, limit);
    Map<String, String> query = new HashMap<String, String>();
    if (name != null) {
      query.put("name", name);
    } else if (contributor != null) {
      query.put("contributor", contributor);
    } else if (instrument != null) {
      query.put("instrument", instrument);
    } else if (jiriki != null) {
      query.put("jiriki", jiriki);
    } else {
    }
    List<Score4UserResponse> response = jirikiService.searchAverageScoresByQuery(query, pageReq);
    if (response == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(response);
    }
  }

  @GetMapping("/v1/players/{id}/scores")
  public ResponseEntity<?> getScoresByPlayerId(
      @PathVariable(name = "id") String id,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String contributor,
      @RequestParam(required = false) String instrument,
      @RequestParam(required = false) String jiriki,
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "20") Integer limit) {
    PageRequest pageReq = new PageRequest(page, limit);
    Map<String, String> query = new HashMap<String, String>();
    if (name != null) {
      query.put("name", name);
    } else if (contributor != null) {
      query.put("contributor", contributor);
    } else if (instrument != null) {
      query.put("instrument", instrument);
    } else if (jiriki != null) {
      query.put("jiriki", jiriki);
    } else {
    }
    List<Score4UserResponse> response = jirikiService.searchScoresByQuery(id, query, pageReq);
    if (response == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(response);
    }
  }

  @GetMapping("/v1/songs/{id}")
  public ResponseEntity<?> getSongBySongId(@PathVariable(name = "id") String id) {
    SongsResponse response = jirikiService.getSongBySongId(id);
    if (response == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(response);
    }
  }

  @GetMapping("/v1/songs/{id}/scores")
  public ResponseEntity<?> getScoresBySongId(@PathVariable(name = "id") String id) {
    List<Score4SongResponse> response = jirikiService.getScoresBySongId(id);
    if (response == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(response);
    }
  }
  
  @GetMapping("/v2/songs/{id}/scores")
  public ResponseEntity<?> getScoresBySongIdV2(@PathVariable(name = "id") String id) {
	List<Score4SongResponseV2> response = jirikiService.getScoresBySongIdV2(id);
	if (response == null) {
	  return ResponseEntity.notFound().build();
	} else {
	  return ResponseEntity.ok(response);
	}
  }

  @GetMapping("/v1/jiriki")
  public ResponseEntity<?> getSongByJiriki() {
    return ResponseEntity.ok().build();
  }

  @PutMapping("/v1/scores")
  public ResponseEntity<?> registerScore(@RequestBody ScoreRequest request) {
    System.out.println(request);
    jirikiService.registerScore(request);
    jirikiService.messagingTest(request);
    return ResponseEntity.accepted().build();
  }
}

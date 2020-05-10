package io.github.gogotea55t.jiriki.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.gogotea55t.jiriki.domain.SongService;
import io.github.gogotea55t.jiriki.domain.request.PageRequest;
import io.github.gogotea55t.jiriki.domain.response.Score4SongResponse;
import io.github.gogotea55t.jiriki.domain.response.Score4SongResponseV2;
import io.github.gogotea55t.jiriki.domain.response.Score4UserResponse;
import io.github.gogotea55t.jiriki.domain.response.Score4UserResponseV2;
import io.github.gogotea55t.jiriki.domain.response.SongsResponse;

@Controller
public class SongController {
  private SongService songService;

  public SongController(SongService songService) {
    this.songService = songService;
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
    Object result = songService.searchSongsByQuery(query, pageReq);
    System.out.println(result);
    return ResponseEntity.ok(result);
  }



  @GetMapping("/v1/players/average/scores")
  public ResponseEntity<?> getAverage(
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
    List<Score4UserResponse> response = songService.searchAverageScoresByQuery(query, pageReq);
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
    List<Score4UserResponse> response = songService.searchScoresByQuery(id, query, pageReq);
    if (response == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(response);
    }
  }

  @GetMapping("/v2/players/{id}/scores")
  public ResponseEntity<?> getScoresByPlayerIdV2(
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
    List<Score4UserResponseV2> response = songService.searchScoresByQueryV2(id, query, pageReq);
    if (response == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(response);
    }
  }

  @GetMapping("/v1/songs/{id}")
  public ResponseEntity<?> getSongBySongId(@PathVariable(name = "id") String id) {
    SongsResponse response = songService.getSongBySongId(id);
    if (response == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(response);
    }
  }

  @GetMapping("/v1/songs/random")
  public ResponseEntity<?> getSongByRandom() {
    SongsResponse response = songService.getSongByRandom();
    if (response == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(response);
    }
  }

  @GetMapping("/v1/songs/{id}/scores")
  public ResponseEntity<?> getScoresBySongId(@PathVariable(name = "id") String id) {
    List<Score4SongResponse> response = songService.getScoresBySongId(id);
    if (response == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(response);
    }
  }

  @GetMapping("/v2/songs/{id}/scores")
  public ResponseEntity<?> getScoresBySongIdV2(@PathVariable(name = "id") String id) {
    List<Score4SongResponseV2> response = songService.getScoresBySongIdV2(id);
    if (response == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(response);
    }
  }
}
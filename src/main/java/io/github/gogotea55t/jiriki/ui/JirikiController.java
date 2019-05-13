package io.github.gogotea55t.jiriki.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.gogotea55t.jiriki.domain.JirikiService;
import io.github.gogotea55t.jiriki.domain.Score4SongResponse;
import io.github.gogotea55t.jiriki.domain.Score4UserResponse;
import io.github.gogotea55t.jiriki.domain.SongsResponse;
import io.github.gogotea55t.jiriki.domain.UserResponse;
import io.github.gogotea55t.jiriki.domain.vo.JirikiRank;

@Controller
public class JirikiController {

  JirikiService jirikiService;

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
    Pageable pageReq = PageRequest.of(page, limit, Sort.by(Order.asc("jirikiRank"), Order.asc("songId")));

    if (name != null) {
      return ResponseEntity.ok(jirikiService.getSongBySongName(name, pageReq));
    } else if (contributor != null) {
      return ResponseEntity.ok(jirikiService.getSongByContributor(contributor, pageReq));
    } else if (instrument != null) {
      return ResponseEntity.ok(jirikiService.getSongByInstrument(instrument, pageReq));
    } else if (jiriki != null) {
      return ResponseEntity.ok(jirikiService.getSongByJiriki(JirikiRank.getJirikiRankFromRankName(jiriki), pageReq));
    }
    List<SongsResponse> songs = jirikiService.getAllSongs(pageReq);

    return ResponseEntity.ok(songs);
  }

  @GetMapping("/v1/players")
  public ResponseEntity<?> getPlayer(@RequestParam(required = false) String name) {
    if (name == null) {
      List<UserResponse> result = jirikiService.getAllPlayer();
      return ResponseEntity.ok().body(result);
    } else {
      List<UserResponse> result = jirikiService.getPlayerByName(name);
      return ResponseEntity.ok(result);
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

  @GetMapping("/v1/players/{id}/scores")
  public ResponseEntity<?> getScoresByPlayerId(
      @PathVariable(name = "id") String id,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String contributor,
      @RequestParam(required = false) String instrument,
      @RequestParam(required = false) String jiriki,
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "20") Integer limit) {
    PageRequest pageReq = PageRequest.of(page, limit);
    List<Score4UserResponse> response;
    if (name != null) {
      response = jirikiService.getScoresByUserIdAndSongNameWithEmpty(id, name, pageReq);
    } else if (contributor != null) {
      response = jirikiService.getScoresByUserIdAndContributorWithEmpty(id, contributor, pageReq);
    } else if (instrument != null) {
      response = jirikiService.getScoresByUserIdAndInstrumentWithEmpty(id, instrument, pageReq);
    } else if (jiriki != null) {
      response =
          jirikiService.getScoresByUserIdAndJirikiRankWithEmpty(
              id, JirikiRank.getJirikiRankFromRankName(jiriki), pageReq);
    } else {
      response = jirikiService.getScoresByUserIdWithEmpty(id, pageReq);
    }
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

  @GetMapping("/v1/jiriki")
  public ResponseEntity<?> getSongByJiriki() {
    return ResponseEntity.ok().build();
  }
}

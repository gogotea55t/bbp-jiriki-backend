package io.github.gogotea55t.jiriki.domain.response;

import java.util.List;

import lombok.Data;

@Data
public class SongTopScoreResponse {
  private List<Score4SongResponseV2> top;
  private List<Score4SongResponseV2> second;
  private List<Score4SongResponseV2> third;

  public SongTopScoreResponse() {}
}

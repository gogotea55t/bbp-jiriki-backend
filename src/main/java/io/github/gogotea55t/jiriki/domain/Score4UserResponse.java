package io.github.gogotea55t.jiriki.domain;

import io.github.gogotea55t.jiriki.domain.entity.Scores;
import io.github.gogotea55t.jiriki.domain.entity.Songs;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Score4UserResponse {
  private String songId;

  private String songName;

  private String contributor;

  private String instrument;

  private Integer score;

  public static Score4UserResponse of(Scores score) {
    Score4UserResponse response = new Score4UserResponse();
    Songs song = score.getSongs();

    response.setSongId(song.getSongId());
    response.setContributor(song.getContributor());
    response.setSongName(song.getSongName());
    response.setInstrument(song.getInstrument());
    response.setScore(score.getScore());

    return response;
  }

  public Score4UserResponse(Object[] obj) {
	this.songId = (String)obj[0];
	this.songName = (String)obj[1];
	this.contributor = (String)obj[2];
	this.instrument = (String)obj[3];
	this.score = (Integer)obj[4];
  }
}

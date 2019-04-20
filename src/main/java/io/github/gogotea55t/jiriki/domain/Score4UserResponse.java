package io.github.gogotea55t.jiriki.domain;

import io.github.gogotea55t.jiriki.domain.entity.Scores;
import io.github.gogotea55t.jiriki.domain.entity.Songs;
import io.github.gogotea55t.jiriki.domain.vo.JirikiRank;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Score4UserResponse {
  private String songId;
  
  private String jirikiRank;

  private String songName;

  private String contributor;

  private String instrument;

  private Integer score;

  public static Score4UserResponse of(Scores score) {
    Score4UserResponse response = new Score4UserResponse();
    Songs song = score.getSongs();

    response.setSongId(song.getSongId());
    response.setJirikiRank(song.getJirikiRank().getJirikiRank());
    response.setContributor(song.getContributor());
    response.setSongName(song.getSongName());
    response.setInstrument(song.getInstrument());
    response.setScore(score.getScore());

    return response;
  }

  public Score4UserResponse(Object[] obj) {
	this.songId = (String)obj[0];
	this.jirikiRank = JirikiRank.getJirikiRankFromId((int)obj[1]).getJirikiRank();
	this.songName = (String)obj[2];
	this.contributor = (String)obj[3];
	this.instrument = (String)obj[4];
	this.score = (Integer)obj[5];
  }
}

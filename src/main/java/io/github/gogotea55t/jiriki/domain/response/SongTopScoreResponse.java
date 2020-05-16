package io.github.gogotea55t.jiriki.domain.response;

import java.util.ArrayList;
import java.util.List;

import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;
import lombok.Data;

@Data
public class SongTopScoreResponse {
  private List<Score4SongResponseV2> top;
  private List<Score4SongResponseV2> second;
  private List<Score4SongResponseV2> third;
  
  public SongTopScoreResponse() {
	 this.top = new ArrayList<>();
	 Score4SongResponseV2 top1 = new Score4SongResponseV2();
	 top1.setUserId("u004");
	 top1.setUserName("あああ");
	 top1.setScore(new ScoreValue("99"));
	 Score4SongResponseV2 top2 = new Score4SongResponseV2();
	 top2.setUserId("u066");
	 top2.setUserName("シン");
	 top2.setScore(new ScoreValue("99"));

	 this.top.add(top1);
	 this.top.add(top2);
	 this.second = new ArrayList<>();
	 Score4SongResponseV2 second1 = new Score4SongResponseV2();
	 second1.setUserId("u015");
	 second1.setUserName("gogo_tea");
	 second1.setScore(new ScoreValue("96"));
	 this.second.add(second1);
	 this.third = new ArrayList<>();
  }
}

package io.github.gogotea55t.jiriki.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

import io.github.gogotea55t.jiriki.domain.vo.JirikiRank;
import io.github.gogotea55t.jiriki.domain.vo.song.Contributor;
import io.github.gogotea55t.jiriki.domain.vo.song.Instrument;
import io.github.gogotea55t.jiriki.domain.vo.song.SongId;
import io.github.gogotea55t.jiriki.domain.vo.song.SongName;
import lombok.Data;
@Data
public class Songs {
  /** 楽曲ID */
  private SongId songId;

  private JirikiRank jirikiRank;

  /** 楽曲名 */
  private SongName songName;

  /** 投稿者名 */
  private Contributor contributor;

  /** 楽器 */
  private Instrument instrument;

  /** 得点 */
  private List<Scores> scores;
  
  private LocalDateTime createdAt;
  
  private String createdBy;
  
  private LocalDateTime updatedAt;
  
  private String updatedBy;

  public static Songs of(
      String songId, JirikiRank jirikiRank, String songName, String contributor, String instrument) {
    Songs song = new Songs();
    song.setSongId(new SongId(songId));
    song.setJirikiRank(jirikiRank);
    song.setSongName(new SongName(songName));
    song.setContributor(new Contributor(contributor));
    song.setInstrument(new Instrument(instrument));
    return song;
  }
}

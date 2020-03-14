package io.github.gogotea55t.jiriki.domain.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import io.github.gogotea55t.jiriki.domain.vo.JirikiRank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
public class Songs {
  /** 楽曲ID */
  private String songId;

  private JirikiRank jirikiRank;

  /** 楽曲名 */
  private String songName;

  /** 投稿者名 */
  private String contributor;

  /** 楽器 */
  private String instrument;

  /** 得点 */
  private List<Scores> scores;

  public static Songs of(
      String songId, JirikiRank jirikiRank, String songName, String contributor, String instrument) {
    Songs song = new Songs();
    song.setSongId(songId);
    song.setJirikiRank(jirikiRank);
    song.setSongName(songName);
    song.setContributor(contributor);
    song.setInstrument(instrument);
    return song;
  }
}

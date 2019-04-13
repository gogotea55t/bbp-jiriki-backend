package io.github.gogotea55t.jiriki.domain.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(exclude = "scores")
@EqualsAndHashCode(exclude = "scores")
@Table
@Entity
public class Songs {
  /** 楽曲ID */
  @Id
  @Column(length = 10)
  private String songId;

  @Column(length = 15)
  private String jirikiRank;

  /** 楽曲名 */
  @Column(length = 60)
  private String songName;

  /** 投稿者名 */
  @Column(length = 20)
  private String contributor;

  /** 楽器 */
  @Column(length = 15)
  private String instrument;

  /** 得点 */
  @Column
  @OneToMany(mappedBy = "songs", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @Fetch(FetchMode.SUBSELECT)
  private List<Scores> scores;

  public static Songs of(
      String songId, String jirikiRank, String songName, String contributor, String instrument) {
    Songs song = new Songs();
    song.setSongId(songId);
    song.setJirikiRank(jirikiRank);
    song.setSongName(songName);
    song.setContributor(contributor);
    song.setInstrument(instrument);
    return song;
  }
}

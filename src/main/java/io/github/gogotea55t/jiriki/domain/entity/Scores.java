package io.github.gogotea55t.jiriki.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;
import lombok.Data;

@Data
@Table
@Entity
public class Scores {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column
  private long scoreId;

  @ManyToOne
  @JoinColumn(foreignKey = @ForeignKey(name = "userId"), nullable = false)
  private Users users;

  @ManyToOne
  @JoinColumn(foreignKey = @ForeignKey(name = "songId"), nullable = false)
  private Songs songs;

  @Column private ScoreValue score;
}

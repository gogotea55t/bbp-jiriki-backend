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

@Table
@Entity
@Data
public class Users {
  /** ユーザー識別ID */
  @Id
  @Column(length = 10)
  private String userId;

  /** ユーザー名 */
  @Column(length = 15)
  private String userName;

  /** スコア */
  @OneToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @Fetch(FetchMode.SUBSELECT)
  private List<Scores> scores;
}

package io.github.gogotea55t.jiriki.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table
@Entity
public class TwitterUsers {
  @Id
  @Column(length = 30)
  private String twitterUserId;
  
  @ManyToOne
  @JoinColumn(foreignKey = @ForeignKey(name = "userId"), nullable = true)
  private Users users;
  
}

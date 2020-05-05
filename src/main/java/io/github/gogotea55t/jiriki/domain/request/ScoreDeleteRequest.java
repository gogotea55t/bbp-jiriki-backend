package io.github.gogotea55t.jiriki.domain.request;

import java.io.Serializable;

import lombok.Data;

@Data
public class ScoreDeleteRequest implements Serializable {
  private static final long serialVersionUID = 1L;

  private String userId;

  private String songId;
}

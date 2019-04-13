package io.github.gogotea55t.jiriki.domain;

import org.springframework.data.domain.Page;

import lombok.Data;

@Data
public class PagesResponse {
  private String first;

  private String prev;

  private String next;

  private String last;

  public PagesResponse(Page page) {}
}

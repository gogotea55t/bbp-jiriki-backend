package io.github.gogotea55t.jiriki.domain.request;

import org.apache.ibatis.session.RowBounds;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PageRequest {
  private int page;
  private int limit;

  public RowBounds getRb() {
    return new RowBounds(page * limit, limit);
  }
}

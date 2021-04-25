package io.github.gogotea55t.jiriki.domain.vo.song;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("unittest")
@SpringBootTest
public class SongIdTest {
  @Test
  public void 楽曲IDを生成できる() {
    new SongId("001");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void 文字列が長すぎる楽曲IDは生成できない() {
	new SongId("123456");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void nullの楽曲IDは生成できない() {
    new SongId(null);
  }
  
  @Test
  public void 同じ値であればequalであると判定できる() {
	SongId a = new SongId("001");
	SongId b = new SongId("001");
	SongId c = new SongId("010");
	assertThat(a).isEqualTo(b);
	assertThat(b).isNotEqualTo(c);
  }
}

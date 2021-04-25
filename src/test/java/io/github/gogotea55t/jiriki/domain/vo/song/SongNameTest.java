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
public class SongNameTest {
  @Test
  public void 文字数が60以内であれば楽曲名を生成できる() {
    new SongName("１２３４５６７８９０２２３４５６７８９０３２３４５６７８９０４２３４５６７８９０５２３４５６７８９０６２３４５６７８９０");
  }

  @Test(expected = IllegalArgumentException.class)
  public void 文字数が60を超える楽曲名は生成できない() {
    new SongName("1234567890223456789032345678904234567890523456789062345678907");
  }

  @Test(expected = IllegalArgumentException.class)
  public void nullの楽曲名が生成できない() {
    new SongName(null);
  }
  
  @Test
  public void 同じ値であればequalsで比較できる() {
	SongName ojamajoCv = new SongName("おジャ魔女カーニバル！！");
	SongName ojamajoCarnival = new SongName("おジャ魔女カーニバル！！");
	SongName oj = new SongName("おジャ魔女カーニバル！！！");
	assertThat(ojamajoCv).isEqualTo(ojamajoCarnival);
	assertThat(ojamajoCv).isNotEqualTo(oj);
  }
}

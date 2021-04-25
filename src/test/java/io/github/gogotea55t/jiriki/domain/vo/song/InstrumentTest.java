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
public class InstrumentTest {
  @Test
  public void 文字数が15文字以内であれば楽器を生成できる() {
    new Instrument("１２３４５６７８９０２２３４５");
  }

  @Test(expected = IllegalArgumentException.class)
  public void 文字数が15文字以上であれば楽器を生成できない() {
    new Instrument("１２３４５６７８９０２２３４５６");
  }

  @Test(expected = IllegalArgumentException.class)
  public void nullがであれば楽器を生成できない() {
    new Instrument(null);
  }

  @Test
  public void 同じ値であればequalsで比較できる() {
    Instrument violin = new Instrument("バイオリン");
    Instrument viola = new Instrument("バイオリン");
    Instrument contrabass = new Instrument("コントラバス");
    assertThat(violin).isEqualTo(viola);
    assertThat(violin).isNotEqualTo(contrabass);
  }
}

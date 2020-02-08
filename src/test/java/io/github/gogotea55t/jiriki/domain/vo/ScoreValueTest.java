package io.github.gogotea55t.jiriki.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("unittest")
@SpringBootTest
public class ScoreValueTest {
  @Test
  public void 成績には0点を付けられる() {
    ScoreValue s = new ScoreValue(0);
    assertThat(s.getScore().toPlainString()).isEqualTo("0");
  }

  @Test
  public void 成績には100点を付けられる() {
    ScoreValue s = new ScoreValue(100);
    assertThat(s.getScore().toPlainString()).isEqualTo("100");
  }

  @Test(expected = IllegalArgumentException.class)
  public void 成績には101点を付けられない() throws Exception {
    new ScoreValue(101);
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void 成績には100点以上を付けられない() throws Exception {
    new ScoreValue(100.001);
    fail();
  }

  

  @Test(expected = IllegalArgumentException.class)
  public void 成績には負の点を付けられない() throws Exception {
    new ScoreValue(-1);
    fail();
  }

  @Test
  public void 成績には小数点がありえる() throws Exception {
    ScoreValue s = new ScoreValue(0.01);
    assertThat(s.getScore().toPlainString()).isEqualTo("0.01");
  }
  
  @Test
  public void 細かすぎる小数は打ち切られる() throws Exception {
	ScoreValue s = new ScoreValue(12.34567);
	assertThat(s.getScore().toPlainString()).isEqualTo("12.35");
  }
  
  @Test
  public void nullはDBに入れられない() throws Exception {
	Double t = null;
	ScoreValue s = new ScoreValue(t);
	assertThat(s.isInsertableToDB()).isFalse();
  }
  
  @Test
  public void doubleはDBに入れられない() throws Exception {
	Double t = 12.3456;
	ScoreValue s = new ScoreValue(t);
	assertThat(s.isInsertableToDB()).isFalse();
  }
  
  @Test
  public void intならDBに入れられる() throws Exception {
	Integer t = 12;
	ScoreValue s = new ScoreValue(t);
	assertThat(s.isInsertableToDB()).isTrue();
  }
  
}

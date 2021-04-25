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
public class ContributorTest {
  @Test
  public void 投稿者名が20文字以内であれば生成できる() {
    new Contributor("１２３４５６７８９０１２３４５６７８９０");
  }

  @Test(expected = IllegalArgumentException.class)
  public void 投稿者名が20文字以上であると生成できない() {
    new Contributor("123456789012345678901");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void nullの投稿者名は生成できない() {
	new Contributor(null);
  }
  
  @Test
  public void 同じ値であればequalsで比較できる() {
	Contributor a1 = new Contributor("aa");
	Contributor a2 = new Contributor("aa");
	Contributor b = new Contributor("bb");
	assertThat(a1).isEqualTo(a2);
	assertThat(a1).isNotEqualTo(b);
  }
}

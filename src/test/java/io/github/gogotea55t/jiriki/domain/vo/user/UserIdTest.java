package io.github.gogotea55t.jiriki.domain.vo.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("unittest")
@SpringBootTest
public class UserIdTest {
  @Test
  public void ユーザIDのインスタンスを生成できる() {
	new UserId("u000000001");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void 長すぎるユーザIDのインスタンスは生成できない() {
	new UserId("uu000000001");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void 中身がnullのインスタンスは生成できない() {
	new UserId(null);
  }
  
  @Test
  public void 中身が同じであることをequalsで判定できる() {
	UserId a = new UserId("u001");
	assertThat(a.equals(new UserId("u001"))).isTrue();
	assertThat(a.equals(new UserId("u002"))).isFalse();
	assertThat(a.equals(null)).isFalse();
  }
  
  @Test
  public void 途中で中身が変わったりしない() {
	UserId a = new UserId("u004");
	String nakami = a.getValue();
	nakami = "u006";
	assertThat(a.equals(new UserId("u004"))).isTrue();
  }
}

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
public class UserNameTest {
  @Test
  public void ユーザ名のインスタンスを生成できる() {
    new UserName("妖怪0012345678912"); // 15文字
  }

  @Test(expected = IllegalArgumentException.class)
  public void 長すぎるユーザ名のインスタンスを生成できない() {
    new UserName("妖怪00123456789123"); // 16文字
  }

  @Test(expected = IllegalArgumentException.class)
  public void 中身がnullのインスタンスは生成できない() {
    new UserName(null);
  }
  
  @Test
  public void 中身が同じであることをequalsで判定できる() {
	UserName b = new UserName("妖怪あいうえお");
	assertThat(b.equals(new UserName("妖怪あいうえお"))).isTrue();
	assertThat(b.equals(new UserName("妖怪かきくけこ"))).isFalse();
	assertThat(b.equals(null)).isFalse();
  }
}

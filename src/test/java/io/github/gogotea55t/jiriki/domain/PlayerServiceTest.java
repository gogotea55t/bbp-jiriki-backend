package io.github.gogotea55t.jiriki.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.gogotea55t.jiriki.domain.entity.TwitterUsers;
import io.github.gogotea55t.jiriki.domain.repository.TwitterUsersRepository;
import io.github.gogotea55t.jiriki.domain.repository.UserRepository;
import io.github.gogotea55t.jiriki.domain.request.TwitterUsersRequest;
import io.github.gogotea55t.jiriki.domain.response.UserResponse;
import io.github.gogotea55t.jiriki.domain.vo.user.UserId;
import io.github.gogotea55t.jiriki.domain.vo.user.UserName;

@RunWith(SpringRunner.class)
@ActiveProfiles("unittest")
@SpringBootTest
public class PlayerServiceTest {

  PlayerService playerService;
  @Autowired UserRepository userRepository;
  @Autowired TwitterUsersRepository twiRepository;

  @Before
  public void init() {
    SampleDatum sample = new SampleDatum();
    userRepository.deleteAll();
    twiRepository.deleteAll();
    userRepository.saveAll(sample.getUsers());
    playerService = new PlayerService(userRepository, twiRepository);
    twiRepository.save(new TwitterUsers("twitterId", sample.getUsers().get(1)));
  }

  @Test
  public void ユーザーの一覧を正常に取得できる() throws Exception {
    assertThat(playerService.getAllPlayer().size()).isEqualTo(2);
  }

  @Test
  public void ユーザー名で検索をかけることができる() throws Exception {
    List<UserResponse> searchResult = playerService.getPlayerByName(new UserName("妖怪1"));
    assertThat(searchResult.size()).isEqualTo(1);
    assertThat(searchResult.get(0).getUserName()).isEqualTo("妖怪1");
    assertThat(searchResult.get(0).getUserId()).isEqualTo("u001");
  }

  @Test
  public void ユーザー名は部分一致で検索する() throws Exception {
    List<UserResponse> searchResult = playerService.getPlayerByName(new UserName("妖怪"));
    assertThat(searchResult.size()).isEqualTo(2);
  }

  @Test
  public void 検索結果は0件でも問題ない() throws Exception {
    List<UserResponse> searchResult = playerService.getPlayerByName(new UserName("人間"));
    assertThat(searchResult.size()).isEqualTo(0);
  }

  @Test
  public void ユーザーIDで検索をかけることができる() throws Exception {
    UserResponse searchResult = playerService.getPlayerById(new UserId("u001"));
    assertThat(searchResult.getUserName()).isEqualTo("妖怪1");
  }

  @Test
  public void 存在しないユーザーIDで検索をしたら何もないが返ってくる() throws Exception {
    UserResponse searchResult = playerService.getPlayerById(new UserId("human"));
    assertThat(searchResult).isNull();
  }

  @Test
  public void twitterのIDとユーザーの紐づけができ登録したものを閲覧できる() throws Exception {
    TwitterUsersRequest testRequest = new TwitterUsersRequest();
    String testTwitterId = "aaaaaaaaaa";
    testRequest.setUserId(new UserId("u001"));
    testRequest.setTwitterUserId(testTwitterId);
    playerService.addNewLinkBetweenUserAndTwitterUser(testRequest);

    Optional<TwitterUsers> putResult = twiRepository.findById(testTwitterId);
    assertThat(putResult.isPresent()).isTrue();
    assertThat(putResult.get().getTwitterUserId()).isEqualTo(testTwitterId);
    assertThat(putResult.get().getUsers().getUserId()).isEqualTo("u001");

    UserResponse getResult = playerService.findPlayerByTwitterId(testTwitterId);
    assertThat(getResult.getUserId()).isEqualTo("u001");
  }

  @Test
  public void 存在しないtwitterアカウントを検索するとnullが返ってくる() throws Exception {
    assertThat(playerService.findPlayerByTwitterId("hogehoge")).isNull();
  }

  @Test(expected = NullPointerException.class)
  public void 存在しないユーザーに対してTwitterアカウントを紐づけようとすると例外が出る() throws Exception {
    TwitterUsersRequest testRequest = new TwitterUsersRequest();
    testRequest.setUserId(new UserId("hogehoge"));
    testRequest.setTwitterUserId("hogehogehoge");

    playerService.addNewLinkBetweenUserAndTwitterUser(testRequest);
  }

  @Test
  public void すでに登録済みだったTwitterIdを別のアカウントに紐づけようとすると新しいアカウントに紐づく() throws Exception {
    String testTwitterId = "twitterId";

    TwitterUsersRequest testRequest = new TwitterUsersRequest();
    testRequest.setTwitterUserId(testTwitterId);
    testRequest.setUserId(new UserId("u001"));

    UserResponse putResult = playerService.addNewLinkBetweenUserAndTwitterUser(testRequest);
    assertThat(putResult.getUserId()).isEqualTo("u001");

    UserResponse getResult = playerService.findPlayerByTwitterId(testTwitterId);
    assertThat(getResult.getUserId()).isEqualTo("u001");

    assertThat(userRepository.findById(new UserId("u002")).isPresent()).isTrue();
  }
}

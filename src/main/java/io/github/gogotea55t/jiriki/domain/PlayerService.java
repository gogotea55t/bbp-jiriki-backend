package io.github.gogotea55t.jiriki.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.gogotea55t.jiriki.domain.entity.TwitterUsers;
import io.github.gogotea55t.jiriki.domain.entity.Users;
import io.github.gogotea55t.jiriki.domain.repository.TwitterUsersRepository;
import io.github.gogotea55t.jiriki.domain.repository.UserRepository;
import io.github.gogotea55t.jiriki.domain.request.TwitterUsersRequest;
import io.github.gogotea55t.jiriki.domain.response.UserResponse;

@Service
public class PlayerService {
  private UserRepository userRepository;
  private TwitterUsersRepository twitterUsersRepository;

  @Autowired
  public PlayerService(UserRepository userRepository, TwitterUsersRepository twiRepository) {
    this.userRepository = userRepository;
    this.twitterUsersRepository = twiRepository;
  }
  
  /**
   * 全プレイヤーの情報を取得し、コントローラーに返却する。
   *
   * @return プレイヤー情報の配列
   */
  public List<UserResponse> getAllPlayer() {
    List<UserResponse> players = new ArrayList<>();
    List<Users> users = userRepository.findAll();
    for (Users user : users) {
      players.add(UserResponse.of(user));
    }
    return players;
  }

  /**
   * プレイヤーを名前で検索し、該当するユーザーの情報を返す。 ユーザーは一人を想定しているが、同名のプレイヤーがいた場合を想定して念のため配列にしている
   *
   * @param name
   * @return
   */
  public List<UserResponse> getPlayerByName(String name) {
    List<UserResponse> players = new ArrayList<>();
    List<Users> users = userRepository.findByUserNameLike(name);
    for (Users user : users) {
      players.add(UserResponse.of(user));
    }

    return players;
  }

  public UserResponse getPlayerById(String id) {
    Optional<Users> response = userRepository.findById(id);
    if (response.isPresent()) {
      return UserResponse.of(response.get());
    } else {
      return null;
    }
  }

  public UserResponse findPlayerByTwitterId(String twitterId) {
    Optional<TwitterUsers> response = twitterUsersRepository.findById(twitterId);
    if (response.isPresent()) {
      return UserResponse.of(response.get().getUsers());
    } else {
      return null;
    }
  }

  @Transactional
  public UserResponse addNewLinkBetweenUserAndTwitterUser(TwitterUsersRequest request) {
    Optional<Users> user = userRepository.findById(request.getUserId());
    if (user.isPresent()) {
      Optional<TwitterUsers> tw = twitterUsersRepository.findById(request.getTwitterUserId());
      if (tw.isPresent()) {
        TwitterUsers newTwitterUsers = tw.get();
        newTwitterUsers.setUsers(user.get());
        twitterUsersRepository.update(newTwitterUsers);
        return UserResponse.of(user.get());
      }
      twitterUsersRepository.save(new TwitterUsers(request.getTwitterUserId(), user.get()));
      return UserResponse.of(user.get());
    } else {
      throw new NullPointerException("User Not Found.");
    }
  }
}

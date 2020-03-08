package io.github.gogotea55t.jiriki.domain.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import io.github.gogotea55t.jiriki.domain.entity.TwitterUsers;

@Mapper
public interface TwitterUsersRepository {
  @Select("SELECT USERID, TWITTER_ID FROM TWITTER_USERS WHERE TWITTER_USER_ID = #{twitterUserId}")
  public Optional<TwitterUsers> findById(String twitterUserId);
  
  @Update("UPDATE TWITTER_USERS SET USER_ID = #{userId} WHERE TWITTER_USER_ID = #{twitterUserId}")
  public void update(TwitterUsers twiUser);
  
  @Insert("INSERT INTO TWITTER_USERS (TWITTER_USER_ID, USER_ID) VALUES (#{twitterUserId}, #{userId})")
  public void save(TwitterUsers twiUser);
}

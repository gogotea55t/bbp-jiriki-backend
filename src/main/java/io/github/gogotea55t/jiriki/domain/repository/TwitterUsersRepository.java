package io.github.gogotea55t.jiriki.domain.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import io.github.gogotea55t.jiriki.domain.entity.TwitterUsers;

@Mapper
public interface TwitterUsersRepository {
  @Select(
      "SELECT tw.TWITTER_USER_ID AS TWITTER_USER_ID , us.USER_ID AS USER_ID, us.USER_NAME AS USER_NAME "
          + "FROM TWITTER_USERS tw "
          + "INNER JOIN USERS us "
          + "ON tw.USERS_USER_ID = us.USER_ID "
          + "WHERE tw.TWITTER_USER_ID = #{twitterUserId}")
  @Results(
      id = "twitterUsers",
      value = {
        @Result(column = "twitter_user_id", property = "twitterUserId"),
        @Result(column = "user_id", property = "users.userId"),
        @Result(column = "user_name", property = "users.userName")
      })
  public Optional<TwitterUsers> findById(String twitterUserId);

  @Update(
      "UPDATE TWITTER_USERS SET USERS_USER_ID = #{users.userId} WHERE TWITTER_USER_ID = #{twitterUserId}")
  public void update(TwitterUsers twiUser);

  @Insert(
      "INSERT INTO TWITTER_USERS (TWITTER_USER_ID, USERS_USER_ID) VALUES (#{twitterUserId}, #{users.userId})")
  public void save(TwitterUsers twiUser);

  @Delete("DELETE FROM TWITTER_USERS")
  public int deleteAll();
}

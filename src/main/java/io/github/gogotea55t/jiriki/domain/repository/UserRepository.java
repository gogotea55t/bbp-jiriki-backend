package io.github.gogotea55t.jiriki.domain.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import io.github.gogotea55t.jiriki.domain.entity.Users;

@Mapper
public interface UserRepository {
  @Select("SELECT USER_ID, USER_NAME FROM USERS WHERE USER_NAME LIKE CONCAT('%', #{userName}, '%')")
  public List<Users> findByUserNameLike(String userName);

  @Select("SELECT USER_ID, USER_NAME FROM USERS WHERE USER_ID = #{userId}")
  public Optional<Users> findById(String userId);

  @Select("SELECT EXISTS(SELECT USER_ID, USER_NAME FROM USERS WHERE USER_ID=#{userId})")
  public boolean existsById(String userId);

  @Select("SELECT USER_ID, USER_NAME FROM USERS")
  public List<Users> findAll();

  @Insert("INSERT INTO USERS (USER_ID, USER_NAME) VALUES (#{userId}, #{userName})")
  public void save(Users user);

  @Insert(
      "<script>"
          + "INSERT INTO USERS (USER_ID, USER_NAME) VALUES "
          + "<foreach item=\"user\" collection=\"list\" separator=\",\"> "
          + "( #{user.userId}, #{user.userName} )"
          + "</foreach>"
          + "</script>")
  public void saveAll(List<Users> users);

  @Update("UPDATE USERS SET USER_NAME = #{userName} WHERE USER_ID = #{userId}")
  public void update(Users user);

  @Delete("DELETE FROM USERS")
  public void deleteAll();
}

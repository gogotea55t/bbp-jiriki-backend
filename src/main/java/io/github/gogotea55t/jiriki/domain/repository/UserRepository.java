package io.github.gogotea55t.jiriki.domain.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;

import io.github.gogotea55t.jiriki.domain.entity.Users;
import io.github.gogotea55t.jiriki.domain.vo.GenericStringValueHandler;
import io.github.gogotea55t.jiriki.domain.vo.user.UserId;
import io.github.gogotea55t.jiriki.domain.vo.user.UserName;

@Mapper
public interface UserRepository {
  @Results(
      id = "user",
      value = {
        @Result(
            javaType = UserId.class,
            column = "USER_ID",
            property = "userId",
            jdbcType = JdbcType.VARCHAR,
            id = true),
        @Result(
            javaType = UserName.class,
            column = "USER_NAME",
            property = "userName",
            jdbcType = JdbcType.VARCHAR,
            id = false)
      })
  @Select(
      "SELECT USER_ID, USER_NAME FROM USERS WHERE USER_NAME LIKE CONCAT('%', #{userName.value}, '%')")
  public List<Users> findByUserNameLike(UserName userName);

  @ResultMap("user")
  @Select("SELECT USER_ID, USER_NAME FROM USERS WHERE USER_ID = #{userId.value}")
  public Optional<Users> findById(UserId userId);

  @Select("SELECT EXISTS(SELECT USER_ID, USER_NAME FROM USERS WHERE USER_ID=#{userId})")
  public boolean existsById(UserId userId);

  @ResultMap("user")
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

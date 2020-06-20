package io.github.gogotea55t.jiriki.domain.vo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import io.github.gogotea55t.jiriki.domain.vo.user.UserId;
import io.github.gogotea55t.jiriki.domain.vo.user.UserName;

@MappedJdbcTypes(value = JdbcType.VARCHAR)
@MappedTypes(value = {UserId.class, UserName.class})
public class GenericStringValueHandler<E extends StringValueObject> extends BaseTypeHandler<E> {
  private Class<E> type;

  public GenericStringValueHandler(Class<E> type) {
    this.type = type;
  }

  @Override
  public void setNonNullParameter(
      PreparedStatement ps, int i, StringValueObject parameter, JdbcType jdbcType)
      throws SQLException {
    ps.setString(i, parameter.getValue());
  }

  @Override
  public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
    try {
      Constructor<E> constructor = type.getDeclaredConstructor(String.class);
      return constructor.newInstance(rs.getString(columnName));
    } catch (InvocationTargetException
        | IllegalAccessException
        | InstantiationException
        | NoSuchMethodException e) {
      throw new SQLException(e);
    }
  }

  @Override
  public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    try {
      Constructor<E> constructor = type.getDeclaredConstructor(String.class);
      return constructor.newInstance(rs.getString(columnIndex));
    } catch (InvocationTargetException
        | IllegalAccessException
        | InstantiationException
        | NoSuchMethodException e) {
      throw new SQLException(e);
    }
  }

  @Override
  public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    try {
      Constructor<E> constructor = type.getDeclaredConstructor(String.class);
      return constructor.newInstance(cs.getString(columnIndex));
    } catch (InvocationTargetException
        | IllegalAccessException
        | InstantiationException
        | NoSuchMethodException e) {
      throw new SQLException(e);
    }
  }
}

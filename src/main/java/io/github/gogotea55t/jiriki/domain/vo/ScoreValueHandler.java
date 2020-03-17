package io.github.gogotea55t.jiriki.domain.vo;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

@Component
public class ScoreValueHandler extends BaseTypeHandler<ScoreValue> {

  @Override
  public void setNonNullParameter(
      PreparedStatement ps, int i, ScoreValue parameter, JdbcType jdbcType) throws SQLException {
    if (parameter.isInsertableToDB()) {
      ps.setBigDecimal(i, parameter.getScore());
    } else {
      throw new IllegalArgumentException("Null or decimal value cannot be inserted into score.");
    }
  }

  @Override
  public ScoreValue getNullableResult(ResultSet rs, String columnName) throws SQLException {
    BigDecimal result = rs.getBigDecimal(columnName);
    return new ScoreValue(result);
  }

  @Override
  public ScoreValue getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    BigDecimal result = rs.getBigDecimal(columnIndex);
    return new ScoreValue(result);
  }

  @Override
  public ScoreValue getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    BigDecimal result = cs.getBigDecimal(columnIndex);
    return new ScoreValue(result);
  }
  
  private void intOrDecimal() {
	  
  }
}

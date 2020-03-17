package io.github.gogotea55t.jiriki.domain.vo;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

@Component
public class JirikiRankTypeHandler extends BaseTypeHandler<JirikiRank> {
  @Override
  public void setNonNullParameter(
      PreparedStatement ps, int i, JirikiRank parameter, JdbcType jdbcType) throws SQLException {
    ps.setInt(i, parameter.getJirikiId());
  }

  @Override
  public JirikiRank getNullableResult(ResultSet rs, String columnName) throws SQLException {
	Integer result = rs.getInt(columnName);
    return JirikiRank.getJirikiRankFromId(result);
  }

  @Override
  public JirikiRank getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    Integer result = rs.getInt(columnIndex);
    return JirikiRank.getJirikiRankFromId(result);
  }

  @Override
  public JirikiRank getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    Integer result = cs.getInt(columnIndex);
    return JirikiRank.getJirikiRankFromId(result);
  }
}

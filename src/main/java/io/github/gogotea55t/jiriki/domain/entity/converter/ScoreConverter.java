package io.github.gogotea55t.jiriki.domain.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;

@Converter(autoApply = false)
public class ScoreConverter implements AttributeConverter<ScoreValue, Integer> {

  @Override
  public Integer convertToDatabaseColumn(ScoreValue attribute) {
    if (attribute.isInsertableToDB()) {
      return attribute.getScore().intValue();
    } else {
      throw new IllegalArgumentException("DBに登録できない成績情報です");
    }
  }

  @Override
  public ScoreValue convertToEntityAttribute(Integer dbData) {
    return new ScoreValue(dbData);
  }
}

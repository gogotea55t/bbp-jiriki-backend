package io.github.gogotea55t.jiriki.domain.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;

@Converter(autoApply = true)
public class ScoreConverter implements AttributeConverter<ScoreValue, Integer> {

  @Override
  public Integer convertToDatabaseColumn(ScoreValue attribute) {
    return attribute.getScore().intValue();
  }

  @Override
  public ScoreValue convertToEntityAttribute(Integer dbData) {
    return new ScoreValue(dbData);
  }
}

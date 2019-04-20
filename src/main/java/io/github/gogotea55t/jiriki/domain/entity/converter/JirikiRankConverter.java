package io.github.gogotea55t.jiriki.domain.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.github.gogotea55t.jiriki.domain.vo.JirikiRank;

@Converter(autoApply = true)
public class JirikiRankConverter implements AttributeConverter<JirikiRank, Integer> {

  @Override
  public Integer convertToDatabaseColumn(JirikiRank attribute) {
    return attribute.getJirikiId();
  }

  @Override
  public JirikiRank convertToEntityAttribute(Integer dbData) {
    return JirikiRank.getJirikiRankFromId(dbData);
  }
}

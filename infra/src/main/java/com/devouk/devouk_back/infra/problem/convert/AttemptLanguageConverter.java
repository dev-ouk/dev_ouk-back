package com.devouk.devouk_back.infra.problem.convert;

import com.devouk.devouk_back.domain.problem.AttemptLanguage;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AttemptLanguageConverter implements AttributeConverter<AttemptLanguage, String> {

  @Override
  public String convertToDatabaseColumn(AttemptLanguage attribute) {
    if (attribute == null) {
      return null;
    }
    return switch (attribute) {
      case CSHARP -> "C#";
      default -> attribute.name();
    };
  }

  @Override
  public AttemptLanguage convertToEntityAttribute(String dbData) {
    if (dbData == null) {
      return null;
    }
    if ("C#".equals(dbData)) {
      return AttemptLanguage.CSHARP;
    }
    return AttemptLanguage.valueOf(dbData);
  }
}

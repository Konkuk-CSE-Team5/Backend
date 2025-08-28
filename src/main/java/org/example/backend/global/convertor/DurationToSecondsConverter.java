package org.example.backend.global.convertor;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;

@Converter(autoApply = false)
public class DurationToSecondsConverter implements AttributeConverter<Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(Duration attribute) {
        if (attribute == null) return null;
        if (attribute.isNegative()) throw new IllegalArgumentException("duration must be >= 0");
        return attribute.getSeconds();
    }

    @Override
    public Duration convertToEntityAttribute(Long dbData) {
        return dbData == null ? null : Duration.ofSeconds(dbData);
    }
}
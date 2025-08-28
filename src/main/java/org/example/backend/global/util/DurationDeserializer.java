package org.example.backend.global.util;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DurationDeserializer extends JsonDeserializer<Duration> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public Duration deserialize(com.fasterxml.jackson.core.JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();
        LocalTime time = LocalTime.parse(text, FORMATTER);
        return Duration.ofSeconds(time.toSecondOfDay());
    }
}

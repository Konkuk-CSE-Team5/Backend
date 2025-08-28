package org.example.backend.global.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationDeserializer extends JsonDeserializer<Duration> {
    // Pattern to match H+:mm:ss format with optional leading '-' (arbitrary-width hours, fixed-width minutes/seconds)
    private static final Pattern DURATION_PATTERN = Pattern.compile("^(-?)(\\d+):(\\d{2}):(\\d{2})$");

    @Override
    public Duration deserialize(com.fasterxml.jackson.core.JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();
        
        if (text == null || text.trim().isEmpty()) {
            throw new JsonParseException(p, "Duration cannot be null or empty, expected [-]H+:mm:ss format");
        }
        
        Matcher matcher = DURATION_PATTERN.matcher(text.trim());
        if (!matcher.matches()) {
            throw new JsonParseException(p, String.format("Invalid duration format, expected [-]H+:mm:ss, got: '%s'", text));
        }
        
        try {
            boolean isNegative = "-".equals(matcher.group(1));
            long hours = Long.parseLong(matcher.group(2));
            long minutes = Long.parseLong(matcher.group(3));
            long seconds = Long.parseLong(matcher.group(4));
            
            // Validate minutes and seconds are within valid ranges
            if (minutes >= 60) {
                throw new JsonParseException(p, String.format("Invalid minutes value: %d (must be 0-59), got: '%s'", minutes, text));
            }
            if (seconds >= 60) {
                throw new JsonParseException(p, String.format("Invalid seconds value: %d (must be 0-59), got: '%s'", seconds, text));
            }
            
            Duration duration = Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds);
            return isNegative ? duration.negated() : duration;
            
        } catch (NumberFormatException e) {
            throw new JsonParseException(p, String.format("Invalid numeric values in duration, expected [-]H+:mm:ss, got: '%s'", text), e);
        }
    }
}

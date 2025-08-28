package org.example.backend.global.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Duration;

public class DurationSerializer extends JsonSerializer<Duration> {

    @Override
    public void serialize(Duration value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        
        long totalSeconds = value.getSeconds();
        boolean isNegative = totalSeconds < 0;
        
        // Use absolute value for calculations, preserve sign
        long absSeconds = Math.abs(totalSeconds);
        
        long hours = absSeconds / 3600;
        long minutes = (absSeconds % 3600) / 60;
        long seconds = absSeconds % 60;
        
        // Format as H+:mm:ss with optional leading '-'
        String formatted = String.format("%s%d:%02d:%02d", 
            isNegative ? "-" : "", 
            hours, 
            minutes, 
            seconds);
            
        gen.writeString(formatted);
    }
}


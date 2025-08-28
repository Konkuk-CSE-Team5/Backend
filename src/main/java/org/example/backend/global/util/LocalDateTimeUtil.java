package org.example.backend.global.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtil {
    public static String toFormattedString(LocalDateTime datetime){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return datetime.format(dateTimeFormatter);
    }
}

package org.example.backend.global.util;

import java.time.Duration;

public class DurationUtil {
    public static String toHHmmss(Duration d) {
        if(d == null){
            return "00:00:00";
        }
        long h = d.toHours();
        long m = d.toMinutesPart(); // Java 9+
        long s = d.toSecondsPart();
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}

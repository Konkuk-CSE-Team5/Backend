package org.example.backend.global.util;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Objects;

public final class AgeUtils {
    private static final ZoneId KOREA = ZoneId.of("Asia/Seoul");
    private static final Clock CLOCK = Clock.system(KOREA);

    private AgeUtils() {}

    /** 오늘(Asia/Seoul 기준) 기준 만 나이 */
    public static int toAge(LocalDate birthDate) {
        return toAge(birthDate, LocalDate.now(CLOCK));
    }

    /** 기준일(onDate) 기준 만 나이 */
    public static int toAge(LocalDate birthDate, LocalDate onDate) {
        Objects.requireNonNull(birthDate, "birthDate must not be null");
        Objects.requireNonNull(onDate, "onDate must not be null");
        if (birthDate.isAfter(onDate)) {
            throw new IllegalArgumentException("birthDate cannot be in the future");
        }
        return Period.between(birthDate, onDate).getYears();
    }
}
package com.example.tproject.util;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Utility helper for time-based comparisons.
 * Used by rules like VelocityRule, OddHourRule, etc.
 */
public class TimeUtils {

    private TimeUtils() {
        // prevent instantiation
    }

    /**
     * Checks whether a timestamp falls within a given duration window from now.
     *
     * Example:
     * isWithinWindow(tx.timestamp, Duration.ofMinutes(1))
     */
    public static boolean isWithinWindow(LocalDateTime timestamp, Duration window) {
        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(timestamp, now);

        return diff.compareTo(window) <= 0;   // MUTATION HOTSPOT
    }

    /**
     * Returns the hour value from a timestamp.
     */
    public static int getHour(LocalDateTime timestamp) {
        return timestamp.getHour();          // MUTATION HOTSPOT
    }

    /**
     * Returns true if timestamp is considered an "odd hour" (0–6 or late night).
     */
    public static boolean isOddHour(LocalDateTime timestamp) {
        int hour = timestamp.getHour();
        return hour < 6 || hour > 23;        // MUTATION HOTSPOT
    }

}

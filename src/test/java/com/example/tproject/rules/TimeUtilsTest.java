package com.example.tproject.rules;

import com.example.tproject.util.TimeUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TimeUtilsTest {
    @Test
    void detectsMidnightAsOdd() {
        assertTrue(TimeUtils.isOddHour(LocalDateTime.of(2025,1,1,0,0)));
    }
//    @Test
//    void detectsEndOfDayOdd() {
//        LocalDateTime t = LocalDateTime.of(2023, 1, 1, 23, 30); // 11 PM
//        assertTrue(TimeUtils.isOddHour(t));
//    }


    @Test
    void correctlyDetectsOddHours() {
        LocalDateTime ts = LocalDateTime.of(2025, 1, 1, 2, 0);
        assertTrue(TimeUtils.isOddHour(ts));
    }

    @Test
    void correctlyDetectsNormalHours() {
        LocalDateTime ts = LocalDateTime.of(2025, 1, 1, 14, 0);
        assertFalse(TimeUtils.isOddHour(ts));
    }
}

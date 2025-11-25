package com.example.tproject.rules;

import com.example.tproject.util.TimeUtils;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

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
void detects23AsNormalHour() {
    LocalDateTime t = LocalDateTime.of(2025, 1, 1, 23, 0);
    assertFalse(TimeUtils.isOddHour(t));
}
    @Test
    void detects6AsNormalHour() {
        LocalDateTime t = LocalDateTime.of(2025, 1, 1, 6, 0);
        assertFalse(TimeUtils.isOddHour(t));
    }
    @Test
    void detects0AsOddHour() {
        LocalDateTime t = LocalDateTime.of(2025, 1, 1, 0, 0);
        assertTrue(TimeUtils.isOddHour(t));
    }
    @Test
    void detects5AsOddHour() {
        LocalDateTime t = LocalDateTime.of(2025, 1, 1, 5, 0);
        assertTrue(TimeUtils.isOddHour(t));
    }
    @Test
    void getHourThrowsOnNull() {
        assertThrows(NullPointerException.class, () -> TimeUtils.getHour(null));
    }
    @Test
    void isWithinWindowTrueForRecentTime() {
        LocalDateTime nowMinus5Sec = LocalDateTime.now().minusSeconds(5);
        assertTrue(TimeUtils.isWithinWindow(nowMinus5Sec, Duration.ofSeconds(10)));
    }
    @Test
    void isWithinWindowFalseForOldTime() {
        LocalDateTime old = LocalDateTime.now().minusSeconds(20);
        assertFalse(TimeUtils.isWithinWindow(old, Duration.ofSeconds(5)));
    }
    @Test
    void isWithinWindowThrowsOnNullTimestamp() {
        assertThrows(Exception.class, () -> TimeUtils.isWithinWindow(null, Duration.ofSeconds(5)));
    }





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

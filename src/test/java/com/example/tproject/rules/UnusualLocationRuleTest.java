package com.example.tproject.rules;

import com.example.tproject.model.RuleResult;
import com.example.tproject.model.Transaction;
import com.example.tproject.model.UserProfile;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UnusualLocationRuleTest {

    private final UnusualLocationRule rule = new UnusualLocationRule();

    @Test
    void triggersForNewLocation() {
        UserProfile user = new UserProfile(1L, "India",
                List.of("Mumbai"), List.of("Android"));

        Transaction tx = new Transaction(
                1L, 1L, 2000, "Delhi", "StoreA", "Android",
                LocalDateTime.now()
        );

        RuleResult result = rule.apply(user, tx, List.of());

        assertTrue(result.isTriggered());
        assertEquals(20, result.getScore());
    }

    @Test
    void doesNotTriggerForUsualLocation() {
        UserProfile user = new UserProfile(1L, "India",
                List.of("Mumbai"), List.of("Android"));

        Transaction tx = new Transaction(
                2L, 1L, 2000, "Mumbai", "StoreA", "Android",
                LocalDateTime.now()
        );

        RuleResult result = rule.apply(user, tx, List.of());

        assertFalse(result.isTriggered());
    }
}

package com.example.tproject.rules;

import com.example.tproject.model.RuleResult;
import com.example.tproject.model.Transaction;
import com.example.tproject.model.UserProfile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HighAmountRuleTest {

    private final HighAmountRule rule = new HighAmountRule();

    @Test
    void triggersWhenAmountIsHigh() {
        UserProfile user = new UserProfile();
        Transaction tx = new Transaction(
                1L, 1L, 60000, "Delhi", "StoreA", "Android",
                LocalDateTime.now()
        );

        RuleResult result = rule.apply(user, tx, List.of());

        assertTrue(result.isTriggered());
        assertEquals(40, result.getScore()); // RiskWeights.HIGH
    }

    @Test
    void doesNotTriggerWhenAmountIsLow() {
        UserProfile user = new UserProfile();
        Transaction tx = new Transaction(
                2L, 1L, 2000, "Delhi", "StoreA", "Android",
                LocalDateTime.now()
        );

        RuleResult result = rule.apply(user, tx, List.of());

        assertFalse(result.isTriggered());
        assertEquals(0, result.getScore());
    }
    @Test
    public void highAmountRuleReturnsZeroScoreWhenNotTriggered() {
        Transaction tx = new Transaction();
        tx.setAmount(499); // below threshold

        HighAmountRule rule = new HighAmountRule();
        RuleResult result = rule.apply(null, tx, List.of());

        Assertions.assertFalse(result.isTriggered());
        Assertions.assertEquals(0, result.getScore());
    }

}

package com.example.tproject.rules;

import com.example.tproject.model.RuleResult;
import com.example.tproject.model.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;



import java.time.LocalDateTime;
import java.util.List;

class VelocityRuleTest {

    private final VelocityRule rule = new VelocityRule();

    @Test
    void triggersWhenTooManyTransactionsInOneMinute() {
        LocalDateTime now = LocalDateTime.now();

        List<Transaction> history = List.of(
                new Transaction(1L, 1L, 10, "A", "M1", "D",
                        now.minusSeconds(10)),
                new Transaction(2L, 1L, 10, "A", "M1", "D",
                        now.minusSeconds(20)),
                new Transaction(3L, 1L, 10, "A", "M1", "D",
                        now.minusSeconds(30))
        );

        Transaction tx = new Transaction(
                4L, 1L, 500, "A", "M1", "D", now
        );

        RuleResult result = rule.apply(null, tx, history);

        assertTrue(result.isTriggered());
        assertEquals(40, result.getScore());
    }
    @Test
    public void velocityRuleDoesNotTriggerWithEmptyHistory() {
        Transaction tx = new Transaction();
        tx.setTimestamp(LocalDateTime.now());

        VelocityRule rule = new VelocityRule();
        RuleResult result = rule.apply(null, tx, List.of());

        Assertions.assertFalse(result.isTriggered());
        Assertions.assertEquals(0, result.getScore());
    }


    @Test
    void doesNotTriggerIfBelowThreshold() {
        List<Transaction> history = List.of();
        Transaction tx = new Transaction(1L, 1L, 10, "A", "B", "C",
                LocalDateTime.now());

        RuleResult result = rule.apply(null, tx, history);

        assertFalse(result.isTriggered());
    }
}

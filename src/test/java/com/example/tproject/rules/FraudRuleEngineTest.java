package com.example.tproject.rules;
import com.example.tproject.model.RuleResult;
import com.example.tproject.model.Transaction;
import com.example.tproject.model.UserProfile;
import com.example.tproject.service.FraudRuleEngine;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class FraudRuleEngineTest {

    @Test
    void returnsOnlyTriggeredRules() {
        FraudRule mockRule = (u, t, h) ->
                new RuleResult("TEST", 10, true, "OK");

        FraudRuleEngine engine = new FraudRuleEngine(List.of(mockRule));

        List<RuleResult> results = engine.evaluateAll(
                new UserProfile(), new Transaction(), List.of()
        );

        assertEquals(1, results.size());
    }

    @Test
    void calculatesTotalScoreCorrectly() {
        FraudRuleEngine engine = new FraudRuleEngine(List.of());

        List<RuleResult> results = List.of(
                new RuleResult("A", 10, true, "x"),
                new RuleResult("B", 20, true, "y")
        );

        assertEquals(30, engine.calculateTotalScore(results));
    }
}

package com.example.tproject.rules;
import com.example.tproject.model.Decision;
import com.example.tproject.service.FraudDecisionEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class FraudDecisionEngineTest {

    private final FraudDecisionEngine engine = new FraudDecisionEngine();
    @Test
    public void returnsBlockForExactly100() {
        FraudDecisionEngine engine = new FraudDecisionEngine();
        Assertions.assertEquals(
                Decision.BLOCK,
                engine.decide(100),     // boundary that PIT mutates heavily
                "Score exactly 100 must BLOCK"
        );
    }

    @Test
    public void returnsReviewForExactly50() {
        FraudDecisionEngine engine = new FraudDecisionEngine();
        Assertions.assertEquals(
                Decision.REVIEW,
                engine.decide(50),      // kills == and >= boundary mutants
                "Score exactly 50 must REVIEW"
        );
    }

    @Test
    void blocksWhenScoreHigh() {
        assertEquals(Decision.BLOCK, engine.decide(100));
    }

    @Test
    void reviewsWhenMedium() {
        assertEquals(Decision.REVIEW, engine.decide(50));
    }

    @Test
    void allowsWhenLow() {
        assertEquals(Decision.ALLOW, engine.decide(10));
    }
}


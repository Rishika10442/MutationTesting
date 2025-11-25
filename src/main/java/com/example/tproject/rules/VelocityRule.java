package com.example.tproject.rules;


import com.example.tproject.model.RuleResult;
import com.example.tproject.model.Transaction;
import com.example.tproject.model.UserProfile;
import com.example.tproject.util.RiskWeights;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class VelocityRule implements FraudRule {

    private static final int MAX_TXN_COUNT = 3;
    private static final Duration WINDOW = Duration.ofMinutes(1);

    @Override
    public RuleResult apply(UserProfile user, Transaction tx, List<Transaction> history) {

        boolean triggered = false;
        int score = 0;
        String message = null;

        LocalDateTime now = LocalDateTime.now();
        long count = history.stream()
                .filter(t -> Duration.between(t.getTimestamp(), now).compareTo(WINDOW) <= 0) // mutation hotspot
                .count();

        if (count >= MAX_TXN_COUNT) {    // mutation hotspot
            triggered = true;
            score = RiskWeights.HIGH;
            message = "Too many transactions in a short time";
        }

        return new RuleResult("VELOCITY", score, triggered, message);
    }
}

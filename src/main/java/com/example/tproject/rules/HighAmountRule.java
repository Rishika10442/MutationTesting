package com.example.tproject.rules;



import com.example.tproject.model.RuleResult;
import com.example.tproject.model.Transaction;
import com.example.tproject.model.UserProfile;
import com.example.tproject.util.RiskWeights;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HighAmountRule implements FraudRule {

    private static final double THRESHOLD = 50000;

    @Override
    public RuleResult apply(UserProfile user, Transaction tx, List<Transaction> history) {

        boolean triggered = false;
        int score = 0;
        String message = null;

        if (tx.getAmount() > THRESHOLD) {    // mutation hotspot
            triggered = true;
            score = RiskWeights.HIGH;
            message = "Transaction amount exceeds " + THRESHOLD;
        }

        return new RuleResult("HIGH_AMOUNT", score, triggered, message);
    }
}

package com.example.tproject.rules;

import com.example.tproject.model.RuleResult;
import com.example.tproject.model.Transaction;
import com.example.tproject.model.UserProfile;
import com.example.tproject.util.RiskWeights;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OddHourRule implements FraudRule {

    @Override
    public RuleResult apply(UserProfile user, Transaction tx, List<Transaction> history) {

        boolean triggered = false;
        int score = 0;
        String message = null;

        int hour = tx.getTimestamp().getHour();   // mutation hotspot

        if (hour < 6 || hour > 23) {             // mutation hotspot
            triggered = true;
            score = RiskWeights.LOW;
            message = "Transaction made at an odd hour: " + hour;
        }

        return new RuleResult("ODD_HOUR", score, triggered, message);
    }
}
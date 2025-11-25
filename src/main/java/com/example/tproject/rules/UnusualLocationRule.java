package com.example.tproject.rules;



import com.example.tproject.model.RuleResult;
import com.example.tproject.model.Transaction;
import com.example.tproject.model.UserProfile;
import com.example.tproject.util.RiskWeights;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UnusualLocationRule implements FraudRule {

    @Override
    public RuleResult apply(UserProfile user, Transaction tx, List<Transaction> history) {

        boolean triggered = false;
        int score = 0;
        String message = null;

        if (!user.getUsualLocations().contains(tx.getLocation())) {   // mutation hotspot
            triggered = true;
            score = RiskWeights.MEDIUM;
            message = "Location " + tx.getLocation() + " is unusual";
        }

        return new RuleResult("UNUSUAL_LOCATION", score, triggered, message);
    }
}

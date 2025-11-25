package com.example.tproject.rules;

import com.example.tproject.model.RuleResult;
import com.example.tproject.model.Transaction;
import com.example.tproject.model.UserProfile;
import com.example.tproject.util.RiskWeights;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlacklistedMerchantRule implements FraudRule {

    private static final List<String> BLACKLIST = List.of(
            "FraudStore",
            "ScamMall",
            "ShadyMarket"
    );

    @Override
    public RuleResult apply(UserProfile user, Transaction tx, List<Transaction> history) {

        boolean triggered = false;
        int score = 0;
        String message = null;

        if (BLACKLIST.contains(tx.getMerchant())) {   // mutation hotspot
            triggered = true;
            score = RiskWeights.CRITICAL;
            message = "Merchant " + tx.getMerchant() + " is blacklisted";
        }

        return new RuleResult("BLACKLISTED_MERCHANT", score, triggered, message);
    }
}
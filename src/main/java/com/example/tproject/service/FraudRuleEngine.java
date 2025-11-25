package com.example.tproject.service;

import com.example.tproject.model.RuleResult;
import com.example.tproject.model.Transaction;
import com.example.tproject.model.UserProfile;
import com.example.tproject.rules.FraudRule;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FraudRuleEngine {

    private final List<FraudRule> rules;

    public FraudRuleEngine(List<FraudRule> rules) {
        this.rules = rules;
    }

    public List<RuleResult> evaluateAll(UserProfile user,
                                        Transaction tx,
                                        List<Transaction> history) {

        List<RuleResult> results = new ArrayList<>();

        for (FraudRule rule : rules) {
            RuleResult result = rule.apply(user, tx, history);   // MUTATION HOTSPOT
            if (result.isTriggered()) {                          // mutated: negated, removed
                results.add(result);
            }
        }
        return results;
    }

    public int calculateTotalScore(List<RuleResult> results) {
        int total = 0;
        for (RuleResult r : results) {
            total += r.getScore();            // arithmetic mutation hotspot
        }
        return total;
    }
}

package com.example.tproject.service;

import com.example.tproject.model.Decision;
import org.springframework.stereotype.Service;

@Service
public class FraudDecisionEngine {

    public Decision decide(int totalScore) {

        // MUTATION HOTSPOTS: boundary operators, negation, return replacement

        if (totalScore >= 80) {
            return Decision.BLOCK;
        } else if (totalScore >= 40) {
            return Decision.REVIEW;
        } else {
            return Decision.ALLOW;
        }
    }

}

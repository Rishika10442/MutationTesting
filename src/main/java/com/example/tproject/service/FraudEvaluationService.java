package com.example.tproject.service;

import com.example.tproject.model.FraudEvaluationResult;
import com.example.tproject.model.RuleResult;
import com.example.tproject.model.Transaction;
import com.example.tproject.model.UserProfile;
import com.example.tproject.model.Decision;

import com.example.tproject.repository.TransactionRepository;
import com.example.tproject.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class FraudEvaluationService {

    private final UserRepository userRepo;
    private final TransactionRepository txRepo;
    private final FraudRuleEngine ruleEngine;
    private final FraudDecisionEngine decisionEngine;
    private final AuditService auditService;

    public FraudEvaluationService(UserRepository userRepo,
                                  TransactionRepository txRepo,
                                  FraudRuleEngine ruleEngine,
                                  FraudDecisionEngine decisionEngine,
                                  AuditService auditService) {
        this.userRepo = userRepo;
        this.txRepo = txRepo;
        this.ruleEngine = ruleEngine;
        this.decisionEngine = decisionEngine;
        this.auditService = auditService;
    }

    public FraudEvaluationResult evaluate(Transaction tx) {

        // === 1. Fetch User ===
        UserProfile user = userRepo.findById(tx.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        // MUTATION HOTSPOT (nulls, missing branch)

        // === 2. Fetch recent transactions ===
        List<Transaction> history =
                txRepo.findRecentByUser(tx.getUserId(), Duration.ofMinutes(10));
        // MUTATION HOTSPOT (method call removal)

        // === 3. Apply Rules ===
        List<RuleResult> results = ruleEngine.evaluateAll(user, tx, history);

        // === 4. Compute total score ===
        int totalScore = ruleEngine.calculateTotalScore(results);

        // === 5. Decision ===
        Decision decision = decisionEngine.decide(totalScore);  // MUTATION HOTSPOT

        // === 6. Audit Log ===
        auditService.logDecision(tx, decision, results);

        // === 7. Save transaction ===
        txRepo.save(tx);   // mutation: remove or swap call

        // === 8. Return result ===
        return new FraudEvaluationResult(decision, totalScore, results);
    }
}

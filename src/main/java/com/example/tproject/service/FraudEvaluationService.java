package com.example.tproject.service;

import com.example.tproject.model.*;
import com.example.tproject.service.RiskHistoryService;

import com.example.tproject.repository.TransactionRepository;
import com.example.tproject.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
//
//@Service
//public class FraudEvaluationService {
//
//    private final UserRepository userRepo;
//    private final TransactionRepository txRepo;
//    private final FraudRuleEngine ruleEngine;
//    private final FraudDecisionEngine decisionEngine;
//    private final AuditService auditService;
//    @Autowired
//    private final RiskHistoryService riskHistoryService;
//
//    public FraudEvaluationService(UserRepository userRepo,
//                                  TransactionRepository txRepo,
//                                  FraudRuleEngine ruleEngine,
//                                  FraudDecisionEngine decisionEngine,
//                                  AuditService auditService) {
//        this.userRepo = userRepo;
//        this.txRepo = txRepo;
//        this.ruleEngine = ruleEngine;
//        this.decisionEngine = decisionEngine;
//        this.auditService = auditService;
//    }
//
//    public FraudEvaluationResult evaluate(Transaction tx) {
//
//        // === 1. Fetch User ===
//        UserProfile user = userRepo.findById(tx.getUserId())
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//        // MUTATION HOTSPOT (nulls, missing branch)
//
//        // === 2. Fetch recent transactions ===
//        List<Transaction> history =
//                txRepo.findRecentByUser(tx.getUserId(), Duration.ofMinutes(10));
//        // MUTATION HOTSPOT (method call removal)
//
//        // === 3. Apply Rules ===
//        List<RuleResult> results = ruleEngine.evaluateAll(user, tx, history);
//
//        // === 4. Compute total score ===
//        int totalScore = ruleEngine.calculateTotalScore(results);
//
//        // === 5. Decision ===
//        Decision decision = decisionEngine.decide(totalScore);  // MUTATION HOTSPOT
//
//        // === 6. Audit Log ===
//        auditService.logDecision(tx, decision, results);
//
//        // === 7. Save transaction ===
//        txRepo.save(tx);   // mutation: remove or swap call
//
//        // === 8. Return result ===
//        return new FraudEvaluationResult(decision, totalScore, results);
//    }
//}
@Service
public class FraudEvaluationService {

    private final UserRepository userRepo;
    private final TransactionRepository txRepo;
    private final FraudRuleEngine ruleEngine;
    private final FraudDecisionEngine decisionEngine;
    private final AuditService auditService;
    private final RiskHistoryService riskHistoryService;  // NO @Autowired here

    @Autowired
    public FraudEvaluationService(UserRepository userRepo,
                                  TransactionRepository txRepo,
                                  FraudRuleEngine ruleEngine,
                                  FraudDecisionEngine decisionEngine,
                                  AuditService auditService,
                                  RiskHistoryService riskHistoryService) {  // <-- Added
        this.userRepo = userRepo;
        this.txRepo = txRepo;
        this.ruleEngine = ruleEngine;
        this.decisionEngine = decisionEngine;
        this.auditService = auditService;
        this.riskHistoryService = riskHistoryService;  // <-- Initialize
    }

    public FraudEvaluationResult evaluate(Transaction tx) {

        // === 1. Fetch User ===
        UserProfile user = userRepo.findById(tx.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // === 2. Fetch recent transactions ===
        List<Transaction> history =
                txRepo.findRecentByUser(tx.getUserId(), Duration.ofMinutes(10));

        // === 3. Apply Rules ===
        List<RuleResult> results = ruleEngine.evaluateAll(user, tx, history);

        // === 4. Compute total score ===
        int totalScore = ruleEngine.calculateTotalScore(results);

        // === 5. Decision ===
        Decision decision = decisionEngine.decide(totalScore);

        // === 6. Audit Log ===
        auditService.logDecision(tx, decision, results);

        // === 7. Save transaction ===
        txRepo.save(tx);

        // === 8. Save Risk History ===
        riskHistoryService.addEntry(
                new RiskHistoryEntry(
                        tx.getId().toString(),
                        tx.getUserId().toString(),
                        decision,
                        RiskLevel.fromScore(totalScore), // or map manually
                        totalScore,
                        LocalDateTime.now(),
                        results.stream().map(RuleResult::getRuleName).toList()
                )
        );

        // === 9. Return result ===
        return new FraudEvaluationResult(decision, totalScore, results);
    }
}

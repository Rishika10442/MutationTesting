package com.example.tproject.service;

import com.example.tproject.model.Decision;
import com.example.tproject.model.RuleResult;
import com.example.tproject.model.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuditService {

    private final List<String> auditLogs = new ArrayList<>();

    public void logDecision(Transaction tx, Decision decision, List<RuleResult> rules) {

        StringBuilder log = new StringBuilder();
        log.append("TX-ID: ").append(tx.getId())
                .append(" | Decision: ").append(decision)
                .append(" | Rules: ").append(rules.stream().map(RuleResult::getRuleName).toList())
                .append(" | Timestamp: ").append(LocalDateTime.now());

        auditLogs.add(log.toString());     // MUTATION HOTSPOT
    }

    public List<String> getAuditLogs() {
        return auditLogs;
    }
}

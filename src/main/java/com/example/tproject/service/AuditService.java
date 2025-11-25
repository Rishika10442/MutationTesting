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

        String log = "TX-ID: " + tx.getId() +
                " | Decision: " + decision +
                " | Rules: " + rules.stream().map(RuleResult::getRuleName).toList() +
                " | Timestamp: " + LocalDateTime.now();

        auditLogs.add(log);     // MUTATION HOTSPOT
    }

    public List<String> getAuditLogs() {
        return auditLogs;
    }
}

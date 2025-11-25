package com.example.tproject.controller;


import com.example.tproject.model.Decision;
import com.example.tproject.model.RiskHistoryEntry;
import com.example.tproject.model.RiskLevel;
import com.example.tproject.service.RiskHistoryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST endpoints to inspect and manage transaction risk history.
 */
@RestController
@RequestMapping("/risk-history")
public class RiskHistoryController {

    private final RiskHistoryService riskHistoryService;

    public RiskHistoryController(RiskHistoryService riskHistoryService) {
        this.riskHistoryService = riskHistoryService;
    }

    /**
     * For manual testing: allow adding an entry via API.
     * In normal flow, entries are added from FraudEvaluationService.
     */
    @PostMapping
    public void addEntry(@RequestBody RiskHistoryEntry entry) {
        riskHistoryService.addEntry(entry);
    }

    @GetMapping
    public List<RiskHistoryEntry> getAll() {
        return riskHistoryService.getAll();
    }

    @GetMapping("/recent")
    public List<RiskHistoryEntry> getRecent(@RequestParam(name = "limit", defaultValue = "50") int limit) {
        return riskHistoryService.getRecent(limit);
    }

    @GetMapping("/user/{userId}")
    public List<RiskHistoryEntry> getByUser(@PathVariable String userId) {
        return riskHistoryService.getByUser(userId);
    }

    @GetMapping("/risk/{riskLevel}")
    public List<RiskHistoryEntry> getByRiskLevel(@PathVariable RiskLevel riskLevel) {
        return riskHistoryService.getByRiskLevel(riskLevel);
    }

    @GetMapping("/decision/{decision}")
    public List<RiskHistoryEntry> getByDecision(@PathVariable Decision decision) {
        return riskHistoryService.getByDecision(decision);
    }

    /**
     * Deletes entries older than the given number of days.
     * Example: DELETE /risk-history/cleanup?days=7
     */
    @DeleteMapping("/cleanup")
    public void cleanupOldEntries(@RequestParam(name = "days", defaultValue = "7") long days) {
        if (days <= 0) {
            return;
        }
        riskHistoryService.deleteOlderThan(Duration.ofDays(days));
    }

    /**
     * Clears all history entries.
     */
    @DeleteMapping
    public void clearAll() {
        riskHistoryService.clearHistory();
    }

    /**
     * Optional: Get entries after a specific timestamp.
     * Example: GET /risk-history/since?timestamp=2025-11-25T10:00:00
     */
    @GetMapping("/since")
    public List<RiskHistoryEntry> getSince(
            @RequestParam("timestamp")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime timestamp) {

        return riskHistoryService.getAll().stream()
                .filter(e -> e.getTimestamp() != null && e.getTimestamp().isAfter(timestamp))
                .toList();
    }
}

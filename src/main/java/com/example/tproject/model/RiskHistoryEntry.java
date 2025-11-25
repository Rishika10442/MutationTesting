package com.example.tproject.model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents one evaluated transaction and its risk outcome.
 */
public class RiskHistoryEntry {

    private String transactionId;
    private String userId;
    private Decision decision;      // ALLOW / REVIEW / BLOCK
    private RiskLevel riskLevel;    // LOW / MEDIUM / HIGH
    private double score;           // Optional - can be 0 if you don't use scoring
    private LocalDateTime timestamp;
    private List<String> triggeredRules = new ArrayList<>();

    public RiskHistoryEntry() {
    }

    public RiskHistoryEntry(String transactionId,
                            String userId,
                            Decision decision,
                            RiskLevel riskLevel,
                            double score,
                            LocalDateTime timestamp,
                            List<String> triggeredRules) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.decision = decision;
        this.riskLevel = riskLevel;
        this.score = score;
        this.timestamp = timestamp;
        if (triggeredRules != null) {
            this.triggeredRules = new ArrayList<>(triggeredRules);
        }
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Decision getDecision() {
        return decision;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getTriggeredRules() {
        return new ArrayList<>(triggeredRules);
    }

    public void setTriggeredRules(List<String> triggeredRules) {
        this.triggeredRules = triggeredRules != null
                ? new ArrayList<>(triggeredRules)
                : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "RiskHistoryEntry{" +
                "transactionId='" + transactionId + '\'' +
                ", userId='" + userId + '\'' +
                ", decision=" + decision +
                ", riskLevel=" + riskLevel +
                ", score=" + score +
                ", timestamp=" + timestamp +
                ", triggeredRules=" + triggeredRules +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RiskHistoryEntry)) return false;
        RiskHistoryEntry that = (RiskHistoryEntry) o;
        return Objects.equals(transactionId, that.transactionId) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, timestamp);
    }
}


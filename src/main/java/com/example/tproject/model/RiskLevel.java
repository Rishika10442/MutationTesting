package com.example.tproject.model;

public enum RiskLevel {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;
    public static RiskLevel fromScore(int score) {

        if (score < 20) {
            return LOW;
        } else if (score < 50) {
            return MEDIUM;
        } else if (score < 80) {
            return HIGH;
        } else {
            return CRITICAL;
        }
    }
}
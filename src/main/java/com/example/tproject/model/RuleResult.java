package com.example.tproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleResult {
    private String ruleName;
    private int score;
    private boolean triggered;
    private String message;   // e.g. "Amount exceeds threshold"

    // constructor, getters, setters
}

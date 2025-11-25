package com.example.tproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FraudEvaluationResult {
    private Decision decision;
    private int totalScore;
    private List<RuleResult> triggeredRules;

    // constructor, getters, setters
}

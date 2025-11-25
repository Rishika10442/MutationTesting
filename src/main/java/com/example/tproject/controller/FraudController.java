package com.example.tproject.controller;


import com.example.tproject.model.FraudEvaluationResult;
import com.example.tproject.model.Transaction;
import com.example.tproject.service.FraudEvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fraud")
public class FraudController {

    private final FraudEvaluationService evaluationService;

    public FraudController(FraudEvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping("/evaluate")
    public ResponseEntity<FraudEvaluationResult> evaluate(@RequestBody Transaction tx) {
        FraudEvaluationResult result = evaluationService.evaluate(tx);
        return ResponseEntity.ok(result);   // MUTATION HOTSPOT: return mutated, removed, etc.
    }
}

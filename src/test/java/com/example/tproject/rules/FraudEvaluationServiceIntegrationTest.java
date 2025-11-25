package com.example.tproject.rules;

import com.example.tproject.model.Decision;
import com.example.tproject.model.FraudEvaluationResult;
import com.example.tproject.model.Transaction;
import com.example.tproject.model.UserProfile;
import com.example.tproject.repository.TransactionRepository;
import com.example.tproject.repository.UserRepository;
import com.example.tproject.service.FraudEvaluationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FraudEvaluationServiceIntegrationTest {

    @Autowired
    private FraudEvaluationService service;
    @Autowired
    private TransactionRepository txRepo;

    @Autowired
    private UserRepository userRepo;

    private List<Transaction> originalTx;
    private List<UserProfile> originalUsers;

    @BeforeEach
    void resetState() {

        // backup original JSON data only once
        if (originalTx == null) {
            originalTx = new ArrayList<>(txRepo.findAll());
        }
        if (originalUsers == null) {
            originalUsers = new ArrayList<>(userRepo.findAll());
        }

        // reset transactions
        txRepo.findAll().clear();
        txRepo.findAll().addAll(originalTx);

        // reset users
        userRepo.findAll().clear();
        userRepo.findAll().addAll(originalUsers);
    }


    @Test
    void evaluatesHighRiskTransaction() {

        Transaction tx = new Transaction(
                5001L,      // id
                1L,         // existing user
                90000,      // high amount → BLOCK
                "Delhi",
                "FraudStore",
                "Android",
                LocalDateTime.now()
        );

        FraudEvaluationResult result = service.evaluate(tx);

        assertEquals(Decision.BLOCK, result.getDecision());
    }

    @Test
    void returnsExceptionWhenUserNotFound() {

        Transaction tx = new Transaction(
                100L,
                9999L,     // non-existing user
                500.0,
                "London",
                "Amazon",
                "iOS",
                LocalDateTime.now()
        );

        // EXPECTS EXCEPTION — matches current service logic
        assertThrows(
                IllegalArgumentException.class,
                () -> service.evaluate(tx)
        );
    }
    @Test
    void allowsLowRiskTransaction() {
        LocalDateTime fixed = LocalDateTime.of(2025, 1, 1, 12, 0);

        Transaction tx = new Transaction(
                101L, 1L, 50,
                "Mumbai", "Shop", "Chrome",
                fixed
        );

        FraudEvaluationResult result = service.evaluate(tx);

        assertEquals(Decision.ALLOW, result.getDecision());
    }



}

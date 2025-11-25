package com.example.tproject.rules;

import com.example.tproject.model.Transaction;
import com.example.tproject.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionRepositoryJsonTest {

    @Autowired
    TransactionRepository repo;
    private List<Transaction> originalTx;

    @BeforeEach
    void resetRepo() {

        // backup original data once
        if (originalTx == null) {
            originalTx = new ArrayList<>(repo.findAll());
        }

        // reset internal list
        repo.findAll().clear();
        repo.findAll().addAll(originalTx);
    }
//    @TempDir
//    File tempDir;


//    @Test
//    void returnsEmptyWhenNoRecentTransactions() {
//        File jsonFile = new File(tempDir, "tx.json");
//        TransactionRepository repo = new TransactionRepository(jsonFile.getAbsolutePath());
//
//        // Add an old transaction
//        repo.save(new Transaction(
//                1L, 1L, 100, "Delhi", "A", "Chrome",
//                LocalDateTime.now().minusHours(5)
//        ));
//
//        List<Transaction> result = repo.findRecentByUser(1L, Duration.ofMinutes(10));
//
//        assertEquals(0, result.size());
//    }
//
//    @Test
//    void savesMultipleTransactions() {
//        File jsonFile = new File(tempDir, "multi.json");
//        TransactionRepository repo = new TransactionRepository(jsonFile.getAbsolutePath());
//
//        Transaction t1 = new Transaction(1L, 1L, 100, "Delhi", "A", "Chrome", LocalDateTime.now());
//        Transaction t2 = new Transaction(2L, 1L, 200, "Delhi", "B", "Android", LocalDateTime.now());
//
//        repo.save(t1);
//        repo.save(t2);
//
//        assertEquals(2, repo.findAll().size());
//    }
@Test
void testFindAllNotEmpty() {
    List<Transaction> list = repo.findAll();
    assertFalse(list.isEmpty());
}

    @Test
    public void savingTwoTransactionsActuallyPersistsBoth() {
        TransactionRepository repo = new TransactionRepository();

        Transaction t1 = new Transaction(
                1L, 1L, 200.0,
                "India",
                "Amazon",
                "Android",
                LocalDateTime.now()
        );

        Transaction t2 = new Transaction(
                2L, 1L, 300.0,
                "India",
                "Flipkart",
                "Chrome",
                LocalDateTime.now()
        );

        repo.save(t1);
        repo.save(t2);

        assertEquals(2, repo.findAll().size());
    }

    @Test
    void savesTransactionToJson() {
        Transaction tx = new Transaction(
                9999L, 1L, 100, "A", "B", "C",
                LocalDateTime.now()
        );

        repo.save(tx);

        List<Transaction> all = repo.findAll();
        assertTrue(all.stream().anyMatch(t -> t.getId().equals(9999L)));
    }
}

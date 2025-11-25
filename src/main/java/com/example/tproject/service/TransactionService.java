package com.example.tproject.service;


import com.example.tproject.model.Transaction;
import com.example.tproject.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository txnRepo;

    public TransactionService(TransactionRepository txnRepo) {
        this.txnRepo = txnRepo;
    }

    public List<Transaction> getRecentTransactions(Long userId, Duration duration) {
        return txnRepo.findRecentByUser(userId, duration);
    }

    public void saveTransaction(Transaction tx) {
        txnRepo.save(tx);
    }

    public List<Transaction> getUserTransactionHistory(Long userId) {
        return txnRepo.findAllByUser(userId);
    }
}

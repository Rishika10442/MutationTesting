package com.example.tproject.repository;

import com.example.tproject.model.Transaction;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TransactionRepository {

    private static final String FILE_PATH = "src/main/resources/data/transactions.json";

    private List<Transaction> transactions = new ArrayList<>();

    // IMPORTANT: Register JavaTimeModule so LocalDateTime works
    private final ObjectMapper mapper =
            new ObjectMapper().registerModule(new JavaTimeModule());

    @PostConstruct
    public void loadTransactions() {
        try {
            transactions = mapper.readValue(
                    new File(FILE_PATH),
                    new TypeReference<List<Transaction>>() {}
            );

            // ensure list is mutable
            transactions = new ArrayList<>(transactions);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load transactions.json", e);
        }
    }

    public List<Transaction> findRecentByUser(Long userId, Duration window) {
        LocalDateTime now = LocalDateTime.now();

        return transactions.stream()
                .filter(t -> t.getUserId().equals(userId))
                .filter(t ->
                        Duration.between(t.getTimestamp(), now).compareTo(window) <= 0
                )
                .collect(Collectors.toList());
    }

    public void save(Transaction tx) {
        transactions.add(tx);
        persist();
    }

    private void persist() {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_PATH), transactions);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write to transactions.json", e);
        }
    }

    public List<Transaction> findAll() {
        return transactions;
    }
    public List<Transaction> findAllByUser(Long userId) {
        return transactions.stream()
                .filter(t -> t.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

}

package com.example.tproject.service;



import com.example.tproject.model.Decision;
import com.example.tproject.model.RiskHistoryEntry;
import com.example.tproject.model.RiskLevel;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * In-memory store of recent risk evaluations.
 * This adds a lot of useful, testable logic and is easy to mutate-test.
 */
@Service
public class RiskHistoryService {

    private static final int DEFAULT_MAX_ENTRIES = 500;

    // Thread-safe list wrapper, but still simple.
    private final List<RiskHistoryEntry> history =
            Collections.synchronizedList(new ArrayList<>());

    private int maxEntries = DEFAULT_MAX_ENTRIES;

    /**
     * Adds a new entry to the history.
     * If the list exceeds maxEntries, oldest entries are removed.
     */
    public void addEntry(RiskHistoryEntry entry) {
        if (entry == null) {
            return;
        }

        synchronized (history) {
            history.add(entry);
            trimToMaxSize();
        }
    }

    private void trimToMaxSize() {
        while (history.size() > maxEntries) {
            history.remove(0);  // remove oldest
        }
    }

    /**
     * Returns a snapshot copy of the full history.
     */
    public List<RiskHistoryEntry> getAll() {
        synchronized (history) {
            return new ArrayList<>(history);
        }
    }

    /**
     * Returns entries for a given user.
     */
    public List<RiskHistoryEntry> getByUser(String userId) {
        if (userId == null || userId.isBlank()) {
            return List.of();
        }
        synchronized (history) {
            return history.stream()
                    .filter(e -> userId.equals(e.getUserId()))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Returns entries for a given risk level.
     */
    public List<RiskHistoryEntry> getByRiskLevel(RiskLevel riskLevel) {
        if (riskLevel == null) {
            return List.of();
        }
        synchronized (history) {
            return history.stream()
                    .filter(e -> riskLevel == e.getRiskLevel())
                    .collect(Collectors.toList());
        }
    }

    /**
     * Returns entries for a given decision (ALLOW/REVIEW/BLOCK).
     */
    public List<RiskHistoryEntry> getByDecision(Decision decision) {
        if (decision == null) {
            return List.of();
        }
        synchronized (history) {
            return history.stream()
                    .filter(e -> decision == e.getDecision())
                    .collect(Collectors.toList());
        }
    }

    /**
     * Returns the most recent N entries (or fewer if not enough entries).
     * Entries are returned in reverse chronological order.
     */
    public List<RiskHistoryEntry> getRecent(int limit) {
        if (limit <= 0) {
            return List.of();
        }
        synchronized (history) {
            int size = history.size();
            if (size == 0) {
                return List.of();
            }
            int fromIndex = Math.max(0, size - limit);
            List<RiskHistoryEntry> slice = history.subList(fromIndex, size);
            List<RiskHistoryEntry> copy = new ArrayList<>(slice);
            Collections.reverse(copy);
            return copy;
        }
    }

    /**
     * Deletes entries older than the given age.
     * For example, age = 7 days means "keep only entries from last 7 days".
     */
    public void deleteOlderThan(Duration age) {
        if (age == null || age.isNegative() || age.isZero()) {
            return;
        }

        LocalDateTime cutoff = LocalDateTime.now().minus(age);

        synchronized (history) {
            Iterator<RiskHistoryEntry> iterator = history.iterator();
            while (iterator.hasNext()) {
                RiskHistoryEntry entry = iterator.next();
                if (entry.getTimestamp() != null &&
                        entry.getTimestamp().isBefore(cutoff)) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * Removes all history entries.
     */
    public void clearHistory() {
        synchronized (history) {
            history.clear();
        }
    }

    /**
     * Allows configuring the maximum number of entries kept in memory.
     */
    public void setMaxEntries(int maxEntries) {
        if (maxEntries > 0) {
            this.maxEntries = maxEntries;
            synchronized (history) {
                trimToMaxSize();
            }
        }
    }

    public int getMaxEntries() {
        return maxEntries;
    }

    public int size() {
        synchronized (history) {
            return history.size();
        }
    }
}


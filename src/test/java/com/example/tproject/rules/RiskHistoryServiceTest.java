package com.example.tproject.rules;



import com.example.tproject.model.Decision;
import com.example.tproject.model.RiskHistoryEntry;
import com.example.tproject.model.RiskLevel;
import com.example.tproject.service.RiskHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RiskHistoryServiceTest {

    private RiskHistoryService service;

    @BeforeEach
    void setup() {
        service = new RiskHistoryService();
        service.setMaxEntries(10); // small limit for testing trim
    }

    private RiskHistoryEntry createEntry(String txId, String userId, RiskLevel level, Decision decision) {
        return new RiskHistoryEntry(
                txId,
                userId,
                decision,
                level,
                50.0,
                LocalDateTime.now(),
                List.of("Rule1", "Rule2")
        );
    }
    @Test
    void testGetByUserNull() {
        List<RiskHistoryEntry> result = service.getByUser(null);
        assertTrue(result.isEmpty());
    }
    @Test
    void testGetByRiskLevelNull() {
        assertTrue(service.getByRiskLevel(null).isEmpty());
    }
    @Test
    void testGetRecentZero() {
        assertTrue(service.getRecent(0).isEmpty());
    }
    @Test
    void testGetRecentNegative() {
        assertTrue(service.getRecent(-5).isEmpty());
    }
    @Test
    void testDeleteOlderThanZero() {
        service.addEntry(createEntry("T1", "U1", RiskLevel.LOW, Decision.ALLOW));
        service.deleteOlderThan(Duration.ZERO);
        assertEquals(1, service.size());   // no deletion should occur
    }

    @Test
    void testGetAllReturnsCopy() {
        RiskHistoryEntry e = createEntry("T1", "U1", RiskLevel.HIGH, Decision.BLOCK);
        service.addEntry(e);

        List<RiskHistoryEntry> list = service.getAll();
        list.clear();

        assertEquals(1, service.size()); // ensures internal list isn’t mutated
    }


    @Test
    void testAddAndGetAll() {
        service.addEntry(createEntry("T1", "U1", RiskLevel.HIGH, Decision.BLOCK));
        service.addEntry(createEntry("T2", "U2", RiskLevel.LOW, Decision.ALLOW));

        List<RiskHistoryEntry> all = service.getAll();
        assertEquals(2, all.size());
    }

    @Test
    void testGetByUser() {
        service.addEntry(createEntry("T1", "U1", RiskLevel.HIGH, Decision.BLOCK));
        service.addEntry(createEntry("T2", "U2", RiskLevel.LOW, Decision.ALLOW));

        List<RiskHistoryEntry> result = service.getByUser("U1");
        assertEquals(1, result.size());
        assertEquals("T1", result.get(0).getTransactionId());
    }

    @Test
    void testGetByRiskLevel() {
        service.addEntry(createEntry("T1", "U1", RiskLevel.HIGH, Decision.BLOCK));
        service.addEntry(createEntry("T2", "U2", RiskLevel.LOW, Decision.ALLOW));

        List<RiskHistoryEntry> high = service.getByRiskLevel(RiskLevel.HIGH);
        assertEquals(1, high.size());
    }

    @Test
    void testGetByDecision() {
        service.addEntry(createEntry("T1", "U1", RiskLevel.HIGH, Decision.BLOCK));
        service.addEntry(createEntry("T2", "U2", RiskLevel.LOW, Decision.ALLOW));

        List<RiskHistoryEntry> blocked = service.getByDecision(Decision.BLOCK);
        assertEquals(1, blocked.size());
    }

    @Test
    void testDeleteOlderThan() {
        LocalDateTime oldTime = LocalDateTime.now().minusDays(10);

        RiskHistoryEntry oldEntry = new RiskHistoryEntry(
                "T1", "U1", Decision.BLOCK, RiskLevel.HIGH, 90.0, oldTime, List.of());
        RiskHistoryEntry newEntry = createEntry("T2", "U1", RiskLevel.LOW, Decision.ALLOW);

        service.addEntry(oldEntry);
        service.addEntry(newEntry);

        service.deleteOlderThan(Duration.ofDays(7));

        List<RiskHistoryEntry> all = service.getAll();
        assertEquals(1, all.size());
        assertEquals("T2", all.get(0).getTransactionId());
    }

    @Test
    void testTrimMaxEntries() {
        // Add 12 entries → should keep only last 10
        for (int i = 1; i <= 12; i++) {
            service.addEntry(createEntry("T" + i, "U1", RiskLevel.LOW, Decision.ALLOW));
        }

        assertEquals(10, service.size());
        assertEquals("T3", service.getAll().get(0).getTransactionId());
    }

    @Test
    void testClearHistory() {
        service.addEntry(createEntry("T1", "U1", RiskLevel.HIGH, Decision.BLOCK));
        service.clearHistory();
        assertEquals(0, service.size());
    }
}


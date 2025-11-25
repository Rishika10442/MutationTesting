package com.example.tproject.rules;


import com.example.tproject.controller.RiskHistoryController;
import com.example.tproject.model.Decision;
import com.example.tproject.model.RiskHistoryEntry;
import com.example.tproject.model.RiskLevel;
import com.example.tproject.service.RiskHistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RiskHistoryController.class)
public class RiskHistoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RiskHistoryService service;

    @Test
    void testGetAll() throws Exception {
        when(service.getAll()).thenReturn(List.of(
                new RiskHistoryEntry("T1", "U1", Decision.ALLOW, RiskLevel.LOW, 10.0,
                        LocalDateTime.now(), List.of("RuleA"))
        ));

        mvc.perform(get("/risk-history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionId").value("T1"))
                .andExpect(jsonPath("$[0].userId").value("U1"));
    }

    @Test
    void testGetByUser() throws Exception {
        when(service.getByUser("U1")).thenReturn(List.of(
                new RiskHistoryEntry("T1", "U1", Decision.BLOCK, RiskLevel.HIGH, 90.0,
                        LocalDateTime.now(), List.of("RuleX"))
        ));

        mvc.perform(get("/risk-history/user/U1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionId").value("T1"));
    }
    @Test
    void testCleanupWithZeroDaysDoesNotThrow() throws Exception {
        mvc.perform(delete("/risk-history/cleanup?days=0"))
                .andExpect(status().isOk());
    }
    @Test
    void testGetRecentZero() throws Exception {
        when(service.getRecent(0)).thenReturn(List.of());

        mvc.perform(get("/risk-history/recent?limit=0"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
    @Test
    void testGetRecentNegative() throws Exception {
        when(service.getRecent(-5)).thenReturn(List.of());

        mvc.perform(get("/risk-history/recent?limit=-5"))
                .andExpect(status().isOk());
    }
    @Test
    void testGetSinceWithFutureTimestampReturnsEmpty() throws Exception {
        String futureTs = LocalDateTime.now().plusDays(1).toString();

        when(service.getAll()).thenReturn(List.of()); // controller filters, not service

        mvc.perform(get("/risk-history/since")
                        .param("timestamp", futureTs))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
    @Test
    void testAddEntryWithEmptyBody() throws Exception {
        mvc.perform(post("/risk-history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());

        verify(service, times(1)).addEntry(any());
    }
    @Test
    void testGetSinceInvalidTimestamp() throws Exception {
        mvc.perform(get("/risk-history/since")
                        .param("timestamp", "not-a-valid-date"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testAddValidHistoryEntry() throws Exception {
        String body = """
    {
       "transactionId": "T10",
       "userId": "U10",
       "decision": "ALLOW",
       "riskLevel": "LOW",
       "score": 10
    }
    """;

        mvc.perform(post("/risk-history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        verify(service, times(1)).addEntry(any());
    }

    @Test
    void testGetSinceReturnsMatchingEntries() throws Exception {
        LocalDateTime baseTime = LocalDateTime.of(2025, 1, 1, 10, 0);

        RiskHistoryEntry e = new RiskHistoryEntry(
                "T100", "U5", Decision.REVIEW, RiskLevel.MEDIUM,
                45.0,
                baseTime.plusHours(2), // occurs AFTER timestamp → returned
                List.of("RuleY")
        );

        when(service.getAll()).thenReturn(List.of(e));

        mvc.perform(get("/risk-history/since")
                        .param("timestamp", baseTime.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionId").value("T100"));
    }


    @Test
    void testCleanupWithNegativeDays() throws Exception {
        mvc.perform(delete("/risk-history/cleanup?days=-1"))
                .andExpect(status().isOk());

        verify(service, never()).deleteOlderThan(any());
    }

    @Test
    void testClearHistory() throws Exception {
        mvc.perform(delete("/risk-history"))
                .andExpect(status().isOk());

        verify(service, times(1)).clearHistory();
    }
}

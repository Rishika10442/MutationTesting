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
    void testClearHistory() throws Exception {
        mvc.perform(delete("/risk-history"))
                .andExpect(status().isOk());

        verify(service, times(1)).clearHistory();
    }
}

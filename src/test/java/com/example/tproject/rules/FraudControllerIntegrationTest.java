package com.example.tproject.rules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FraudControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Test
    public void returnsBadRequestForInvalidTransaction() throws Exception {
        String invalidJson = """
        { "userId": 1, "amount": -50 }
        """;

        mockMvc.perform(post("/fraud/evaluate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isNotFound());

    }

    @Test
    void evaluatesTransactionSuccessfully() throws Exception {

        String request = """
        {
          "id": 3001,
          "userId": 1,
          "amount": 60000,
          "location": "Delhi",
          "merchant": "StoreA",
          "device": "Android",
          "timestamp": "2025-01-23T10:10:10"
        }
        """;

        mockMvc.perform(post("/api/fraud/evaluate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.decision").exists())
                .andExpect(jsonPath("$.totalScore").exists());
    }
}

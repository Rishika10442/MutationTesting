package com.example.tproject.rules;



import com.example.tproject.controller.FraudController;
import com.example.tproject.model.Decision;
import com.example.tproject.model.FraudEvaluationResult;
import com.example.tproject.service.FraudEvaluationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FraudController.class)
public class FraudControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FraudEvaluationService evaluationService;

    @Test
    void testEvaluateReturnsOk() throws Exception {
        FraudEvaluationResult mockRes = new FraudEvaluationResult(
                Decision.ALLOW, 10, List.of()
        );

        when(evaluationService.evaluate(any())).thenReturn(mockRes);

        mvc.perform(post("/api/fraud/evaluate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        { "id": 1, "userId": 1, "amount": 500 }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.decision").value("ALLOW"))
                .andExpect(jsonPath("$.totalScore").value(10));

        verify(evaluationService, times(1)).evaluate(any());
    }

//    @Test
//    void testEvaluateServiceThrows() throws Exception {
//        when(evaluationService.evaluate(any()))
//                .thenThrow(new RuntimeException("boom"));
//
//        mvc.perform(post("/api/fraud/evaluate")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("""
//                    { "id": 1, "userId": 1, "amount": 300 }
//                    """))
//                .andExpect(result ->
//                        assertTrue(result.getResolvedException() instanceof RuntimeException))
//                .andExpect(result ->
//                        assertEquals("boom", result.getResolvedException().getMessage()));
//    }



    @Test
    void testEvaluateWithNullBody() throws Exception {
        mvc.perform(post("/api/fraud/evaluate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().is4xxClientError());
    }
    @Test
    void testEvaluateReturnsNull() throws Exception {
        when(evaluationService.evaluate(any())).thenReturn(null);

        mvc.perform(post("/api/fraud/evaluate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
            { "id": 1, "userId": 1, "amount": 500 }
            """))
                .andExpect(status().isOk())    // controller returns 200
                .andExpect(content().string("")); // response body empty
    }

    @Test
    void testEvaluateInvalidJson() throws Exception {
        mvc.perform(post("/api/fraud/evaluate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isBadRequest());
    }
}

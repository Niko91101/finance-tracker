package com.github.niko91101.financetracker.controller;

import com.github.niko91101.financetracker.mapper.TransactionMapper;
import com.github.niko91101.financetracker.service.TransactionService;
import com.github.niko91101.financetracker.service.UserService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@ImportAutoConfiguration(JacksonAutoConfiguration.class)
public class TransactionControllerTest {

    @MockitoBean
    TransactionService transactionService;

    @MockitoBean
    TransactionMapper transactionMapper;


    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName(value = "Должен вернуть 400, отсутствует обязательный параметр userId")
    void shouldReturnBadRequestWhenFilterWithoutRequestParam() throws Exception {

        mockMvc.perform(
                get("/transaction/filter"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName(value = "Должен вернуть 400 при некорректном типе транзакции")
    void shouldReturnBadRequestWhenTransactionTypeIsInvalid() throws Exception {

        mockMvc.perform(
                get("/transaction/filter")
                        .param("userId", "1")
                        .param("type", "HELLO")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.method").value("GET"))
                .andExpect(jsonPath("$.path").value("/transaction/filter"));

        verifyNoInteractions(transactionService);
    }
}

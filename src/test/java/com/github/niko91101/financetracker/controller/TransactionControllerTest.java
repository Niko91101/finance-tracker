package com.github.niko91101.financetracker.controller;

import com.github.niko91101.financetracker.dto.request.CreateTransactionRequest;
import com.github.niko91101.financetracker.dto.response.TransactionResponse;
import com.github.niko91101.financetracker.mapper.TransactionMapper;
import com.github.niko91101.financetracker.service.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
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

    @ParameterizedTest
    @ValueSource(strings = {
            "1.999",
            "0.001",
            "123456789012345678.12"
    })
    @DisplayName(value = "Должен вернуть 400 при некорректной денежной сумме, которая не проходит Bean Validation")
    void shouldReturnBadRequestWhenAmountIsInvalid(String amount) throws Exception {

        mockMvc.perform(
                post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": %s,
                                  "description": "валидное",
                                  "categoryId" : 1,
                                  "userId" : 1
                                }""".formatted(amount)
                        )
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.method").value("POST"))
                .andExpect(jsonPath("$.path").value("/transaction"))
                .andExpect(jsonPath("$.fieldErrors.amount").exists());

        verifyNoInteractions(transactionService);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1.99",
            "99999999999999999.99"
    })
    @DisplayName("Должен вернуть 201 при валидном значении amount")
    void shouldReturnCreatedWhenAmountIsValid(String amount) throws Exception {

        when(transactionService.saveTransaction(any(CreateTransactionRequest.class))).thenReturn(
                TransactionResponse.builder().id(1L).build()
        );
        mockMvc.perform(
                post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": %s,
                                  "description": "валидное",
                                  "categoryId" : 1,
                                  "userId" : 1
                                }""".formatted(amount)
                        )
        )
                .andExpect(status().isCreated());

        ArgumentCaptor<CreateTransactionRequest> captor = ArgumentCaptor.forClass(CreateTransactionRequest.class);
        verify(transactionService).saveTransaction(captor.capture());

        CreateTransactionRequest capturedRequest = captor.getValue();

        assertEquals(
                0,
                capturedRequest.getAmount().compareTo(new BigDecimal(amount))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1.999",
            "0.001",
            "123456789012345678.12"
    })
    @DisplayName(value = "Должен вернуть 400 при некорректной денежной сумме, которая не проходит Bean Validation")
    void shouldReturnBadRequestWhenAmountIsInvalidInUpdate(String amount) throws Exception {

        mockMvc.perform(
                        put("/transaction/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                  "amount": %s,
                                  "description": "валидное",
                                  "categoryId" : 1
                                }""".formatted(amount)
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.method").value("PUT"))
                .andExpect(jsonPath("$.path").value("/transaction/1"))
                .andExpect(jsonPath("$.fieldErrors.amount").exists());

        verifyNoInteractions(transactionService);
    }
}

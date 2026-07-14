package com.github.niko91101.financetracker.service;

import com.github.niko91101.financetracker.dto.request.UpdateTransactionRequest;
import com.github.niko91101.financetracker.dto.response.TransactionResponse;
import com.github.niko91101.financetracker.enums.TypeTransactions;
import com.github.niko91101.financetracker.mapper.TransactionMapper;
import com.github.niko91101.financetracker.model.Category;
import com.github.niko91101.financetracker.model.Transaction;
import com.github.niko91101.financetracker.repository.CategoryRepository;
import com.github.niko91101.financetracker.repository.TransactionRepository;
import com.github.niko91101.financetracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private TransactionService transactionService;


    private Transaction transaction;
    private Category category;

    private UpdateTransactionRequest request;
    private TransactionResponse response;

    @BeforeEach
    void setUp() {
        category = Category.builder()

                .id(1L)
                .name("Food")
                .type(TypeTransactions.EXPENSE)
                .build();

        transaction = Transaction.builder()

                .id(1L)
                .amount(100)
                .description("Old description")
                .category(category)
                .build();

        request = UpdateTransactionRequest.builder()

                .categoryId(category.getId())
                .amount(500)
                .description("New description")
                .build();

        response = TransactionResponse.builder()

                .id(transaction.getId())
                .amount(request.getAmount())
                .description(request.getDescription())
                .build();
    }

    @Test
    @DisplayName("Должен обновить managed транзакцию без вызова save")
    void shouldUpdateManagedTransactionWithoutSave() {

        when(transactionRepository.findById(1L))
                .thenReturn(Optional.of(transaction));

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when((transactionMapper.toResponse(transaction)))
                .thenReturn(response);

        TransactionResponse result = transactionService.updateTransaction(1L, request);

        assertNotNull(request);

        assertEquals(500, transaction.getAmount());
        assertEquals("New description", transaction.getDescription());
        assertEquals(category, transaction.getCategory());

        verify(transactionRepository).findById(1L);
        verify(categoryRepository).findById(1L);
        verify(transactionMapper).toResponse(transaction);

        verify(transactionRepository, never()).save(any());
    }



}

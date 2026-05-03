package com.github.niko91101.financetracker.mapper;

import com.github.niko91101.financetracker.dto.request.CreateTransactionRequest;
import com.github.niko91101.financetracker.dto.request.UpdateTransactionRequest;
import com.github.niko91101.financetracker.dto.response.CategoryResponse;
import com.github.niko91101.financetracker.dto.response.TransactionResponse;

import com.github.niko91101.financetracker.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponse toResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .category(CategoryResponse.builder()
                        .id(transaction.getCategory().getId())
                        .name(transaction.getCategory().getName())
                        .type(transaction.getCategory().getType())
                        .build())
                .build();
    }

    public Transaction toEntity(CreateTransactionRequest request) {
        return Transaction.builder()
                .amount(request.getAmount())
                .description(request.getDescription())
                .build();
    }

    public Transaction toEntity(UpdateTransactionRequest request) {
        return Transaction.builder()
                .amount(request.getAmount())
                .description(request.getDescription())
                .build();
    }
}

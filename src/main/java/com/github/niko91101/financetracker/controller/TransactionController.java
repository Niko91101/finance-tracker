package com.github.niko91101.financetracker.controller;

import com.github.niko91101.financetracker.dto.request.CreateTransactionRequest;
import com.github.niko91101.financetracker.dto.request.UpdateTransactionRequest;
import com.github.niko91101.financetracker.dto.response.TransactionResponse;
import com.github.niko91101.financetracker.dto.response.TransactionShortResponse;
import com.github.niko91101.financetracker.enums.TypeTransactions;
import com.github.niko91101.financetracker.mapper.TransactionMapper;
import com.github.niko91101.financetracker.model.Transaction;
import com.github.niko91101.financetracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransaction());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionalById(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TransactionResponse>> getFilteredTransactions(
            @RequestParam Long userId,
            @RequestParam(required = false) TypeTransactions type,
            @RequestParam(required = false)BigDecimal minAmount
            ) {
        return ResponseEntity.ok(
                transactionService.findTransactions(userId, type, minAmount)
                        .stream()
                        .map(transactionMapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/short")
    public ResponseEntity<List<TransactionShortResponse>> getShortTransaction(@RequestParam Long userId) {
        return ResponseEntity.ok(
                transactionService.findShortTransaction(userId));
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> saveTransaction(@Valid @RequestBody CreateTransactionRequest transaction) {
        TransactionResponse savedTransaction = transactionService.saveTransaction(transaction);

        return ResponseEntity.created(URI.create("transaction/" + savedTransaction.getId())).body(savedTransaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(@Valid @RequestBody UpdateTransactionRequest transaction, @PathVariable Long id) {
        return ResponseEntity.ok(transactionService.updateTransaction(id, transaction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }


}

package com.github.niko91101.financetracker.controller;

import com.github.niko91101.financetracker.dto.request.CreateTransactionRequest;
import com.github.niko91101.financetracker.dto.request.UpdateTransactionRequest;
import com.github.niko91101.financetracker.dto.response.TransactionResponse;
import com.github.niko91101.financetracker.model.Transaction;
import com.github.niko91101.financetracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransaction());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionalById(id));
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> saveTransaction(@RequestBody CreateTransactionRequest transaction) {
        TransactionResponse savedTransaction = transactionService.saveTransaction(transaction);

        return ResponseEntity.created(URI.create("transaction/" + savedTransaction.getId())).body(savedTransaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(@RequestBody UpdateTransactionRequest transaction, @PathVariable Long id) {
        return ResponseEntity.ok(transactionService.updateTransaction(id, transaction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }



}

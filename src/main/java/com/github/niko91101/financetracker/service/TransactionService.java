package com.github.niko91101.financetracker.service;

import com.github.niko91101.financetracker.exception.TransactionNotFoundException;
import com.github.niko91101.financetracker.model.Transaction;
import com.github.niko91101.financetracker.repository.CategoryRepository;
import com.github.niko91101.financetracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public List<Transaction> getAllTransaction() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionalById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    public Transaction saveTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Транзакция не может быть пустая");
        }

        transaction.setCategory(categoryRepository.findById(transaction.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Ошибка!")));

        return transactionRepository.save(transaction);
    }

    public Transaction updateTransaction(Long id, Transaction newTransaction) {
        if (newTransaction == null || (!transactionRepository.existsById(id))) {
            throw new IllegalArgumentException("Такой транзакции нет");
        }

        newTransaction.setId(id);
        return transactionRepository.save(newTransaction);
    }

    public void deleteTransaction(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Некорректно передан id");
        }

        transactionRepository.deleteById(id);
    }
}

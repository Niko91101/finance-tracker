package com.github.niko91101.financetracker.service;

import com.github.niko91101.financetracker.exception.TransactionNotFoundException;
import com.github.niko91101.financetracker.model.Transaction;
import com.github.niko91101.financetracker.repository.CategoryRepository;
import com.github.niko91101.financetracker.repository.TransactionRepository;
import com.github.niko91101.financetracker.validation.ValidationUtil;
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

        ValidationUtil.validate(transaction);

        transaction.setCategory(categoryRepository.findById(transaction.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Ошибка!")));

        return transactionRepository.save(transaction);
    }

    public Transaction updateTransaction(Long id, Transaction newTransaction) {
        ValidationUtil.validate(newTransaction);

        if ((!transactionRepository.existsById(id))) {
            throw new IllegalArgumentException("Такой транзакции нет");
        }

        newTransaction.setId(id);
        return transactionRepository.save(newTransaction);
    }

    public void deleteTransaction(Long id) {

        ValidationUtil.validate(id);

        transactionRepository.deleteById(id);
    }
}

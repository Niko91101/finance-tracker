package com.github.niko91101.financetracker.service;

import com.github.niko91101.financetracker.enums.TypeTransactions;
import com.github.niko91101.financetracker.exception.TransactionNotFoundException;
import com.github.niko91101.financetracker.model.Transaction;
import com.github.niko91101.financetracker.repository.CategoryRepository;
import com.github.niko91101.financetracker.repository.TransactionRepository;
import com.github.niko91101.financetracker.validation.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Integer sumTransactionsWithAmountMore500(List<Transaction> transactions) {
        ValidationUtil.validate(transactions);

        if (transactions.isEmpty()) {
            throw new IllegalArgumentException("Список транзацкий пуст");
        }

        return transactions.stream()
                .mapToInt(Transaction::getAmount)
                .filter(amount -> amount >= 500)
                .sum();
    }

    public Map<TypeTransactions, List<Transaction>> getTransactionOnCategory(List<Transaction> transactions) {
        return transactions.stream()
               .collect(Collectors.groupingBy((transaction ->  transaction.getCategory().getType())));
    }
}

package com.github.niko91101.financetracker.service;

import com.github.niko91101.financetracker.enums.TypeTransactions;
import com.github.niko91101.financetracker.model.Transaction;
import com.github.niko91101.financetracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {

    private final TransactionRepository transactionRepository;


    public Integer getBalance(Long userId) {

        List<Transaction> transactions = transactionRepository.findByUserId(userId);

        int income = transactions.stream()
                .filter(transaction -> transaction.getCategory().getType().equals(TypeTransactions.INCOME))
                .mapToInt(Transaction::getAmount)
                .sum();

        int expense = transactions.stream()
                .filter(transaction -> transaction.getCategory().getType().equals(TypeTransactions.EXPENSE))
                .mapToInt(Transaction::getAmount)
                .sum();

        return income - expense;
    }

    public Integer getTotalIncome(long userId) {
        return sumByType(userId, TypeTransactions.INCOME);
    }

    public Integer getTotalExpense(long userId) {
        return sumByType(userId, TypeTransactions.EXPENSE);
    }

    private Integer sumByType(Long userId, TypeTransactions type) {
        return transactionRepository.findByUserId(userId).stream()
                .filter(transaction -> transaction.getCategory().getType().equals(type))
                .mapToInt(Transaction::getAmount)
                .sum();
    }
}

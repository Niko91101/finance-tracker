package com.github.niko91101.financetracker.service;

import com.github.niko91101.financetracker.enums.TypeTransactions;
import com.github.niko91101.financetracker.model.Transaction;
import com.github.niko91101.financetracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {

    private final TransactionRepository transactionRepository;


    public BigDecimal getBalance(Long userId) {

        List<Transaction> transactions = transactionRepository.findByUserId(userId);

        BigDecimal income = transactions.stream()
                .filter(transaction -> transaction.getCategory().getType().equals(TypeTransactions.INCOME))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expense = transactions.stream()
                .filter(transaction -> transaction.getCategory().getType().equals(TypeTransactions.EXPENSE))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return income.subtract(expense);
    }

    public BigDecimal getTotalIncome(long userId) {
        return sumByType(userId, TypeTransactions.INCOME);
    }

    public BigDecimal getTotalExpense(long userId) {
        return sumByType(userId, TypeTransactions.EXPENSE);
    }

    private BigDecimal sumByType(Long userId, TypeTransactions type) {
        return transactionRepository.findByUserId(userId).stream()
                .filter(transaction -> transaction.getCategory().getType().equals(type))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

package com.github.niko91101.financetracker.service;

import com.github.niko91101.financetracker.enums.TypeTransactions;
import com.github.niko91101.financetracker.model.Transaction;
import com.github.niko91101.financetracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
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
}

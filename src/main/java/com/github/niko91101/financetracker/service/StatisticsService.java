package com.github.niko91101.financetracker.service;

import com.github.niko91101.financetracker.dto.response.CategoryStatisticsResponse;
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
        BigDecimal income = sumByType(userId, TypeTransactions.INCOME);
        BigDecimal expense = sumByType(userId, TypeTransactions.EXPENSE);

        return income.subtract(expense);
    }

    public BigDecimal getTotalIncome(long userId) {
        return sumByType(userId, TypeTransactions.INCOME);
    }

    public BigDecimal getTotalExpense(long userId) {
        return sumByType(userId, TypeTransactions.EXPENSE);
    }

    public List<CategoryStatisticsResponse> findStatisticsByUserId(Long userId) {
        return transactionRepository.findStatisticsByUserId(userId);
    }

    private BigDecimal sumByType(Long userId, TypeTransactions type) {
        return transactionRepository
                .sumAmountByUserIdAndType(userId, type);
    }
}

package com.github.niko91101.financetracker.service;

import com.github.niko91101.financetracker.dto.response.CategoryStatisticsResponse;
import com.github.niko91101.financetracker.enums.TypeTransactions;
import com.github.niko91101.financetracker.exception.StatisticsNotFoundException;
import com.github.niko91101.financetracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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

    public List<CategoryStatisticsResponse> findStatistics(
            Long userId,
            BigDecimal minAmount,
            TypeTransactions type
    ) {
        if (minAmount == null && type == null) {
            return transactionRepository.findStatisticsByUserId(userId);
        }

        if (minAmount != null && type == null) {
            return transactionRepository.findStatisticsByUserIdAndMinAmount(userId, minAmount);
        }

        if (minAmount == null && type != null) {
            return transactionRepository.findStatisticsByUserIdAndTypeTransaction(userId, type);
        }

        return transactionRepository.findStatisticsByUserIdAndTypeTransactionAndMinAmount(userId, type, minAmount);
    }

    public CategoryStatisticsResponse findTopStatisticsByUserIdAndCategoryType(Long userId, TypeTransactions type) {
        List<CategoryStatisticsResponse> statistics = transactionRepository.findTopStatisticsByUserIdAndCategoryType(
                userId,
                type,
                PageRequest.of(0, 1)
        );
        return statistics.stream()
                .findFirst()
                .orElseThrow(() -> new StatisticsNotFoundException(userId, type));
    }

    private BigDecimal sumByType(Long userId, TypeTransactions type) {
        return transactionRepository
                .sumAmountByUserIdAndType(userId, type);
    }


}

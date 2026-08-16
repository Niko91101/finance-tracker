package com.github.niko91101.financetracker.service;

import com.github.niko91101.financetracker.dto.response.CategoryStatisticsResponse;
import com.github.niko91101.financetracker.enums.TypeTransactions;
import com.github.niko91101.financetracker.exception.StatisticsNotFoundException;
import com.github.niko91101.financetracker.exception.TransactionNotFoundException;
import com.github.niko91101.financetracker.model.Transaction;
import com.github.niko91101.financetracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

    public List<CategoryStatisticsResponse> findStatisticsByUserId(Long userId) {
        return transactionRepository.findStatisticsByUserId(userId);
    }

    public List<CategoryStatisticsResponse> findStatisticsByUserIdAndMinAmount(Long userId, BigDecimal minAmount) {
        return transactionRepository.findStatisticsByUserIdAndMinAmount(userId, minAmount);
    }

    public List<CategoryStatisticsResponse> findStatisticsByUserIdAndTypeTransaction(
            Long userId, TypeTransactions type) {
        return transactionRepository.findStatisticsByUserIdAndTypeTransaction(userId, type);
    }

//    public List<CategoryStatisticsResponse> findStatisticsByUserIdAndMinAmountAndTypeTransaction(Long userId, TypeTransactions type, BigDecimal minAmount) {
//        return transactionRepository.findStatisticsByUserIdAndTypeTransactionAndMinAmount(userId, type, minAmount);
//    }

    public CategoryStatisticsResponse findTopStatisticsByUserIdAndTypeTransaction(Long userId, TypeTransactions type) {
        List<CategoryStatisticsResponse> statistics = transactionRepository.findTopStatisticsByUserIdAndTypeTransaction(
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

package com.github.niko91101.financetracker.repository;

import com.github.niko91101.financetracker.dto.response.CategoryStatisticsResponse;
import com.github.niko91101.financetracker.dto.response.TransactionShortResponse;
import com.github.niko91101.financetracker.enums.TypeTransactions;
import com.github.niko91101.financetracker.model.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>,
        JpaSpecificationExecutor<Transaction> {

    List<Transaction> findByUserId(Long userId);

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            JOIN t.category c
            WHERE t.user.id = :userId
                AND c.type = :type
            """)
    BigDecimal sumAmountByUserIdAndType(
            @Param("userId") Long userId,
            @Param("type") TypeTransactions type
    );

    @Query("""
            SELECT new com.github.niko91101.financetracker.dto.response.CategoryStatisticsResponse(
                c.name,
                COUNT(t),
                SUM(t.amount)
            )
            FROM Transaction t
            JOIN t.category c
            WHERE t.user.id = :userId
                AND c.type = :type
            GROUP BY c.name
            ORDER BY SUM(t.amount) DESC
            """)
    List<CategoryStatisticsResponse> findTopStatisticsByUserIdAndCategoryType(
            @Param("userId") Long userId,
            @Param("type") TypeTransactions type,
            Pageable pageable
    );

    @Query("""
            SELECT new com.github.niko91101.financetracker.dto.response.TransactionShortResponse(
                t.amount,
                t.description,
                c.name
            )
            FROM Transaction t
            JOIN t.category c
            WHERE t.user.id = :userId
            """)
    List<TransactionShortResponse> findShortTransactionByUserId(
            @Param("userId") Long userId
    );

    @Query("""
            SELECT new com.github.niko91101.financetracker.dto.response.CategoryStatisticsResponse(
                c.name,
                COUNT(t),
                SUM(t.amount)
            )
            FROM Transaction t
            JOIN t.category c
            WHERE t.user.id = :userId
                AND (:type IS NULL OR c.type = :type)
            GROUP BY c.name
            HAVING (:minAmount IS NULL OR SUM(t.amount) > :minAmount)
            ORDER BY SUM(t.amount) DESC
            """)
    List<CategoryStatisticsResponse> findStatistics(
            @Param("userId") Long userId,
            @Param("type") TypeTransactions type,
            @Param("minAmount") BigDecimal minAmount
    );
}

package com.github.niko91101.financetracker.repository;

import com.github.niko91101.financetracker.dto.response.CategoryStatisticsResponse;
import com.github.niko91101.financetracker.enums.TypeTransactions;
import com.github.niko91101.financetracker.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

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
            @Param("type") TypeTransactions typeTransactions
    );

    @Query("""
            SELECT new com.github.niko91101.financetracker.dto.response.CategoryStatisticsResponse(
                        c.name,
                        SUM(t.amount)
                        )
            FROM Transaction  t 
            JOIN t.category c 
            WHERE t.user.id = :userId
            GROUP BY c.name
            """)
    List<CategoryStatisticsResponse> findStatisticsByUserId(
            @Param("userId") Long userId
    );
}

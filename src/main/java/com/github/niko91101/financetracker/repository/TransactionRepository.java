package com.github.niko91101.financetracker.repository;

import com.github.niko91101.financetracker.dto.response.CategoryStatisticsResponse;
import com.github.niko91101.financetracker.enums.TypeTransactions;
import com.github.niko91101.financetracker.model.Transaction;
import com.github.niko91101.financetracker.model.User;
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
            @Param("type") TypeTransactions typeTransactions
    );

    @Query("""
            SELECT new com.github.niko91101.financetracker.dto.response.CategoryStatisticsResponse(
                        c.name,
                        COUNT(t),
                        SUM(t.amount)
                        )
            FROM Transaction  t
            JOIN t.category c
            WHERE t.user.id = :userId
            GROUP BY c.name
            ORDER BY SUM(t.amount) DESC
            """)
    List<CategoryStatisticsResponse> findStatisticsByUserId(
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
            GROUP BY c.name
            HAVING SUM(t.amount) > :minAmount
            ORDER BY SUM(t.amount) DESC
            """)
    List<CategoryStatisticsResponse> findStatisticsByUserIdAndMinAmount(
            @Param("userId") Long userId,
            @Param("minAmount") BigDecimal minAmount
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
    List<CategoryStatisticsResponse> findStatisticsByUserIdAndTypeTransaction(
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
            HAVING SUM(t.amount) > :minAmount
            ORDER BY SUM(t.amount) DESC
            """)
    List<CategoryStatisticsResponse> findStatisticsByUserIdAndTypeTransactionAndMinAmount(
            @Param("userId") Long userId,
            @Param("type") TypeTransactions type,
            @Param("minAmount") BigDecimal minAmount
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
    List<CategoryStatisticsResponse> findTopStatisticsByUserIdAndTypeTransaction(
            @Param("userId") Long userId,
            @Param("type") TypeTransactions type,
            Pageable pageable
    );

    //временный
    @Query("""
            SELECT t
            FROM Transaction t
            JOIN FETCH t.category c
            JOIN FETCH t.user u
            WHERE t.user.id = :userId
            """)
    List<Transaction> findByUserIdWithDetails(
            @Param("userId") Long userId
    );
}

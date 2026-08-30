package com.github.niko91101.financetracker.repository;

import com.github.niko91101.financetracker.IntegrationTestBase;
import com.github.niko91101.financetracker.dto.response.CategoryStatisticsResponse;
import com.github.niko91101.financetracker.enums.TypeTransactions;
import com.github.niko91101.financetracker.model.Category;
import com.github.niko91101.financetracker.model.Transaction;
import com.github.niko91101.financetracker.model.User;
import com.github.niko91101.financetracker.util.CategoryTestFactory;
import com.github.niko91101.financetracker.util.TransactionTestFactory;
import com.github.niko91101.financetracker.util.UserTestFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
public class TransactionRepositoryTest extends IntegrationTestBase {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldReturnStatistics() {

        User user = userRepository.save(UserTestFactory.createDefaultUser());

        Category food = categoryRepository.save(CategoryTestFactory.createCategory(
                "Еда",
                TypeTransactions.EXPENSE
        ));

        Category taxi = categoryRepository.save(CategoryTestFactory.createCategory(
                "Такси",
                TypeTransactions.EXPENSE
        ));

        Category salary = categoryRepository.save(CategoryTestFactory.createCategory(
                "Зарплата",
                TypeTransactions.INCOME
        ));

        Transaction transaction1 = transactionRepository.save(
                TransactionTestFactory.createTransaction(
                        new BigDecimal("500.00"),
                        "Описание первой транзакции",
                        user,
                        food
                )
        );

        Transaction transaction2 = transactionRepository.save(
                TransactionTestFactory.createTransaction(
                        new BigDecimal("700.00"),
                        "Описание второй транзакции",
                        user,
                        food
                )
        );

        Transaction transaction3 = transactionRepository.save(
                TransactionTestFactory.createTransaction(
                        new BigDecimal("300.00"),
                        "Описание третьей транзакции",
                        user,
                        taxi
                )
        );

        Transaction transaction4 = transactionRepository.save(
                TransactionTestFactory.createTransaction(
                        new BigDecimal("50000"),
                        "Описание четвертой транзакции",
                        user,
                        salary
                )
        );

        List<CategoryStatisticsResponse> result = transactionRepository.findStatistics(
                user.getId(),
                TypeTransactions.EXPENSE,
                null
        );

        assertEquals(2, result.size());
        assertEquals("Еда", result.getFirst().categoryName());

        assertEquals(2, result.getFirst().transactionCount());
        assertEquals(new BigDecimal("1200.00"), result.getFirst().totalAmount());

        assertEquals("Такси", result.get(1).categoryName());
        assertEquals(1, result.get(1).transactionCount());
        assertEquals(new BigDecimal("300.00"), result.get(1).totalAmount());
    }


}

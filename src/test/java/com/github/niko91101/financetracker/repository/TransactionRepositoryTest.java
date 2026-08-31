package com.github.niko91101.financetracker.repository;

import com.github.niko91101.financetracker.IntegrationTestBase;
import com.github.niko91101.financetracker.dto.response.CategoryStatisticsResponse;
import com.github.niko91101.financetracker.enums.TypeTransactions;
import com.github.niko91101.financetracker.model.Category;
import com.github.niko91101.financetracker.model.User;
import com.github.niko91101.financetracker.util.CategoryTestFactory;
import com.github.niko91101.financetracker.util.TransactionTestFactory;
import com.github.niko91101.financetracker.util.UserTestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserTestFactory.createDefaultUser());

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

        transactionRepository.save(
                TransactionTestFactory.createTransaction(
                        new BigDecimal("500.00"),
                        "Описание первой транзакции",
                        user,
                        food
                )
        );

        transactionRepository.save(
                TransactionTestFactory.createTransaction(
                        new BigDecimal("700.00"),
                        "Описание второй транзакции",
                        user,
                        food
                )
        );

        transactionRepository.save(
                TransactionTestFactory.createTransaction(
                        new BigDecimal("300.00"),
                        "Описание третьей транзакции",
                        user,
                        taxi
                )
        );

        transactionRepository.save(
                TransactionTestFactory.createTransaction(
                        new BigDecimal("50000"),
                        "Описание четвертой транзакции",
                        user,
                        salary
                )
        );
    }
    @Test
    @DisplayName(value = "Должен вернуть статистику пользователя по его id и типу транзакции")
    void shouldReturnStatisticsByUserIdAndTypeTransactions() {

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

    @Test
    @DisplayName(value = "Должен вернуть статистику по id пользователя")
    void shouldReturnStatisticsByUserId() {

        List<CategoryStatisticsResponse> result = transactionRepository.findStatistics(
                user.getId(),
                null,
                null
        );

        assertEquals(3, result.size());
        assertEquals("Зарплата", result.getFirst().categoryName());
        assertEquals("Такси", result.getLast().categoryName());
        assertEquals("Еда", result.get(1).categoryName());

        assertEquals(1, result.getFirst().transactionCount());
        assertEquals(new BigDecimal("50000.00"), result.getFirst().totalAmount());

        assertEquals(2, result.get(1).transactionCount());
        assertEquals(new BigDecimal("1200.00"), result.get(1).totalAmount());

        assertEquals(1, result.getLast().transactionCount());
        assertEquals(new BigDecimal("300.00"), result.getLast().totalAmount());

    }

    @Test
    @DisplayName(value = "Должен вернуть статистику пользователя по минимальной сумме по категории 1000 УЕ")
    void shouldReturnStatisticsByUserIdAndMinAmount() {
        List<CategoryStatisticsResponse> result = transactionRepository.findStatistics(
                user.getId(),
                null,
                new BigDecimal("1000.00")
        );

        assertEquals(2, result.size());
        assertEquals("Зарплата", result.getFirst().categoryName());
        assertEquals("Еда", result.get(1).categoryName());

        assertEquals(1, result.getFirst().transactionCount());
        assertEquals(new BigDecimal("50000.00"), result.getFirst().totalAmount());

        assertEquals(2, result.get(1).transactionCount());
        assertEquals(new BigDecimal("1200.00"), result.get(1).totalAmount());
    }

    @Test
    @DisplayName(value = "Должен вернуть статистику пользователя по тратам и минимальной сумме по категории 1000")
    void shouldReturnStatisticsByUserIdAndTypeTransactionAndMinAmount() {
        List<CategoryStatisticsResponse> result = transactionRepository.findStatistics(
                user.getId(),
                TypeTransactions.EXPENSE,
                new BigDecimal("1000.00")
        );

        assertEquals(1, result.size());
        assertEquals("Еда", result.getFirst().categoryName());
        assertEquals(2, result.getFirst().transactionCount());
        assertEquals(new BigDecimal("1200.00"), result.getFirst().totalAmount());
    }

    @Test
    @DisplayName("Должен вернуть пустой список при переданной минимальной сумме по категории трат 2000")
    void shouldReturnEmptyList() {
        List<CategoryStatisticsResponse> result = transactionRepository.findStatistics(
                user.getId(),
                TypeTransactions.EXPENSE,
                new BigDecimal("2000.00")
        );

        assertTrue(result.isEmpty());
    }
}

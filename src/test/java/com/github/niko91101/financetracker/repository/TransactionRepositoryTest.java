package com.github.niko91101.financetracker.repository;

import com.github.niko91101.financetracker.IntegrationTestBase;
import com.github.niko91101.financetracker.dto.response.CategoryStatisticsResponse;
import com.github.niko91101.financetracker.enums.TypeTransactions;
import com.github.niko91101.financetracker.model.Category;
import com.github.niko91101.financetracker.model.Transaction;
import com.github.niko91101.financetracker.model.User;
import com.github.niko91101.financetracker.specification.TransactionSpecification;
import com.github.niko91101.financetracker.util.CategoryTestFactory;
import com.github.niko91101.financetracker.util.TransactionTestFactory;
import com.github.niko91101.financetracker.util.UserTestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
                        new BigDecimal("50000.00"),
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

    @Test
    @DisplayName("Должен вернуть первую страницу отфильтрованных транзакций ")
    void shouldReturnFirstPageOfFilteredTransaction() {
        Pageable pageable = PageRequest.of(0, 2);
        Specification<Transaction> specification = TransactionSpecification.hasUserId(user.getId());

        Page<Transaction> result = transactionRepository.findAll(specification, pageable);

        assertEquals(4, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertEquals(0, result.getNumber());
        assertEquals(2, result.getNumberOfElements());
        assertTrue(result.isFirst());
        assertFalse(result.isLast());
    }

    @Test
    @DisplayName("Должен вернуть вторую страницу отфильтрованных транзакций")
    void shouldReturnSecondPageOfFilteredTransaction() {
        Specification<Transaction> specification = TransactionSpecification.hasUserId(user.getId());
        Pageable pageable = PageRequest.of(1, 2);

        Page<Transaction> result = transactionRepository.findAll(specification, pageable);

        assertEquals(4, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertEquals(1, result.getNumber());
        assertEquals(2, result.getNumberOfElements());
        assertTrue(result.isLast());
        assertFalse(result.isFirst());
    }

    @Test
    @DisplayName("Должен вернуть отфильтрованные транзакции  отсортированные в обратном порядке по сумме")
    void shouldReturnPageOfFilteredTransactionWithReverseSort() {
        Specification<Transaction> specification = TransactionSpecification.hasUserId(user.getId());
        Pageable pageable = PageRequest.of(0, 4, Sort.by("amount").reverse());

        Page<Transaction> result = transactionRepository.findAll(specification, pageable);

        assertEquals(new BigDecimal("50000.00"), result.getContent().getFirst().getAmount());
        assertEquals(new BigDecimal("700.00"), result.getContent().get(1).getAmount());
        assertEquals(new BigDecimal("500.00"), result.getContent().get(2).getAmount());
        assertEquals(new BigDecimal("300.00"), result.getContent().getLast().getAmount());
    }

    @Test
    @DisplayName("Должен вернуть отфильтрованные транзакции с типом EXPENSE отсортированные в " +
            "обратном порядке по полю amount")
    void shouldReturnPageOfFilteredExpenseWithReverseSort() {
        Specification<Transaction> specification = TransactionSpecification.hasUserId(user.getId());
        specification = specification.and(TransactionSpecification.hasType(TypeTransactions.EXPENSE));
        Pageable pageable = PageRequest.of(0, 2, Sort.Direction.DESC, "amount");

        Page<Transaction> result = transactionRepository.findAll(specification, pageable);

        assertEquals(new BigDecimal("700.00"), result.getContent().getFirst().getAmount());
        assertEquals(new BigDecimal("500.00"), result.getContent().get(1).getAmount());

        assertEquals(3, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertEquals(0, result.getNumber());
        assertEquals(2, result.getNumberOfElements());
    }
}

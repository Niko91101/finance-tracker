package com.github.niko91101.financetracker.mapper;

import com.github.niko91101.financetracker.dto.response.TransactionResponse;
import com.github.niko91101.financetracker.enums.TypeTransactions;
import com.github.niko91101.financetracker.model.Category;
import com.github.niko91101.financetracker.model.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

public class TransactionMapperTest {
    TransactionMapper mapper = new TransactionMapper();

    @Test
    void shouldReturnDateFromTransactionResponse() {
        LocalDate date = LocalDate.of(1995, Month.JUNE,2);
        Transaction transaction = Transaction.builder()
                .id(1L)
                .date(date)
                .amount(new BigDecimal("1000.00"))
                .description("Описание")
                .category(new Category(1L,"Еда", TypeTransactions.EXPENSE))
                .build();

        TransactionResponse response = mapper.toResponse(transaction);

        Assertions.assertEquals(date, response.getDate());
    }
}

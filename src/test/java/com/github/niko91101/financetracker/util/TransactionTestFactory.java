package com.github.niko91101.financetracker.util;

import com.github.niko91101.financetracker.model.Category;
import com.github.niko91101.financetracker.model.Transaction;
import com.github.niko91101.financetracker.model.User;

import java.math.BigDecimal;

public class TransactionTestFactory {

    public static Transaction createTransaction(
            BigDecimal amount,
            String description,
            User user,
            Category category
    ) {
        return Transaction.builder()
                .amount(amount)
                .description(description)
                .user(user)
                .category(category)
                .build();

    }
}

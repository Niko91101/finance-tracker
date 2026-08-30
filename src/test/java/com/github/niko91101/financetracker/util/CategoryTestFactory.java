package com.github.niko91101.financetracker.util;

import com.github.niko91101.financetracker.enums.TypeTransactions;
import com.github.niko91101.financetracker.model.Category;

public class CategoryTestFactory {

    public static Category createCategory(
            String name,
            TypeTransactions type
    ) {
        return Category.builder()
                .name(name)
                .type(type)
                .build();
    }
}

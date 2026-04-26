package com.github.niko91101.financetracker.enums;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum TypeTransactions {
    INCOME("Доход"),
    EXPENSE("Расход");

    private final String description;

    TypeTransactions(String description) {
        this.description = description;
    }
}

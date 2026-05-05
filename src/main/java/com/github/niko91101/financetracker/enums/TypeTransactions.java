package com.github.niko91101.financetracker.enums;

public enum TypeTransactions {
    INCOME("Доход"),
    EXPENSE("Расход");

    private final String description;

    TypeTransactions(String description) {
        this.description = description;
    }
}

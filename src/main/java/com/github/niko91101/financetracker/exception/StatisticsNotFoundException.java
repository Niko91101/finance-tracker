package com.github.niko91101.financetracker.exception;

import com.github.niko91101.financetracker.enums.TypeTransactions;

public class StatisticsNotFoundException extends RuntimeException {
    public StatisticsNotFoundException(Long userId, TypeTransactions type) {
        super("Статистика для " + userId + " и типа " + type + " не найдена");
    }
}

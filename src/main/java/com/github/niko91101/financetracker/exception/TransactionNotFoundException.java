package com.github.niko91101.financetracker.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(Long id) {
        super("Транзакция с id " + id + " не существует");
    }
}

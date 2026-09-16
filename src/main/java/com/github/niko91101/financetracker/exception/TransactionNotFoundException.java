package com.github.niko91101.financetracker.exception;

public class TransactionNotFoundException extends NotFoundException {
    public TransactionNotFoundException(Long id) {
        super("Транзакция с id " + id + " не существует");
    }
}

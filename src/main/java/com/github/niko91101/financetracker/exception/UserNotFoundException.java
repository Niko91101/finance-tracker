package com.github.niko91101.financetracker.exception;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(Long id) {
        super("Пользователь с ID: " + id + " не найден" );
    }
}

package com.github.niko91101.financetracker.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("Категории с ID: " + id + " не существует");
    }
}

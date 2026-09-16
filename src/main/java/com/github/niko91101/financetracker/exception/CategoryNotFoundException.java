package com.github.niko91101.financetracker.exception;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(Long id) {
        super("Категории с ID: " + id + " не существует");
    }
}

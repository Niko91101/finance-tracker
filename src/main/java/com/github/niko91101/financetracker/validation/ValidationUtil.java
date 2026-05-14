package com.github.niko91101.financetracker.validation;

public class ValidationUtil {
    private ValidationUtil() {

    }

    public static void validate(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
    }

    public static void validate(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        if (id <= 0) {
            throw new IllegalArgumentException("ID cannot be a negative number");
        }
    }
}

package com.github.niko91101.financetracker.validation;

public class ValidationUtil {
    private ValidationUtil() {

    }

    public static void validate(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
    }
}

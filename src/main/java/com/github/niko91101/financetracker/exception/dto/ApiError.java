package com.github.niko91101.financetracker.exception.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ApiError(
        LocalDateTime timestamp,
        int status,
        String message,
        String method,
        String path,
        Map<String, List<String>> fieldErrors
) {
}

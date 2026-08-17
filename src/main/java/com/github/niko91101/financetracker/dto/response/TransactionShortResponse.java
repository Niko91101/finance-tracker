package com.github.niko91101.financetracker.dto.response;

import java.math.BigDecimal;

public record TransactionShortResponse(
        BigDecimal amount,
        String description,
        String categoryName
) {
}

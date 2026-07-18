package com.github.niko91101.financetracker.dto.response;

import java.math.BigDecimal;

public record CategoryStatisticsResponse(
        String categoryName,
        BigDecimal totalAmount
) {
}

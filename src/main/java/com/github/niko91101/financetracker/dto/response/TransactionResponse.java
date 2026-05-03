package com.github.niko91101.financetracker.dto.response;

import com.github.niko91101.financetracker.enums.TypeTransactions;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TransactionResponse {
    private Long id;
    private Integer amount;
    private String description;
    private LocalDate date;
    private CategoryResponse category;
}

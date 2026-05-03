package com.github.niko91101.financetracker.dto.response;

import com.github.niko91101.financetracker.enums.TypeTransactions;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private TypeTransactions type;
}

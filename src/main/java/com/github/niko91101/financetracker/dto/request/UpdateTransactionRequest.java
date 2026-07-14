package com.github.niko91101.financetracker.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UpdateTransactionRequest {
    @NotNull(message = "Сумма не может быть пустой")
    private Integer amount;

    @Size(min = 5, max = 50, message = "Описание должно быть от 5 до 50 символов")
    private String description;

    @NotNull(message = "ID категории не может быть пустым")
    private Long categoryId;
}

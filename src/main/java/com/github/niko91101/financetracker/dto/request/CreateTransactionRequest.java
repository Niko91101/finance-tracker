package com.github.niko91101.financetracker.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTransactionRequest {

    @NotNull(message = "Сумма не может быть пустой")
    @Positive(message = "Сумма не может быть меньше нуля")
    private BigDecimal amount;

    @Size(min = 5, max = 50, message = "Описание должно быть от 5 до 50 символов")
    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @NotNull(message = "ID категории не может быть пустым")
    @Positive(message = "ID категории не может быть меньше нуля")
    private Long categoryId;

    @NotNull(message = "ID пользователя не может быть пустым")
    @Positive(message = "ID пользователя не может быть меньше нуля")
    private Long userId;
}

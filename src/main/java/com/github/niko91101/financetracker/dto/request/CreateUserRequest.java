package com.github.niko91101.financetracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserRequest {

    @NotBlank(message = "Имя не может быть пустым")
    private String username;
    @NotBlank(message = "Вы не ввели пароль")
    private String password;
}

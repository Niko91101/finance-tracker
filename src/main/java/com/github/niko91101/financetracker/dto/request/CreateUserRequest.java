package com.github.niko91101.financetracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserRequest {

    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 5, max = 12, message = "Имя должно быть от 5 до 12 символов")
    private String username;

    @NotBlank(message = "Вы не ввели пароль")
    @Size(min = 5, max = 12, message = "Пароль должен быть от 5 до 12 символов")

    private String password;
}

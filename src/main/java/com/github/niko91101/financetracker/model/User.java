package com.github.niko91101.financetracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 5, max = 12, message = "Имя должно быть от 5 до 12 символом")
    private String username;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 5, max = 12, message = "Имя должно быть от 5 до 12 символом")
    private String password;

    private LocalDate date = LocalDate.now();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions;
}

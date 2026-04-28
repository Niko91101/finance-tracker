package com.github.niko91101.financetracker.dto.response;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private LocalDate date;
}

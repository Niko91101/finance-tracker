package com.github.niko91101.financetracker.model;

import com.github.niko91101.financetracker.enums.TypeTransactions;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "categories")
@Entity
@Builder
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private TypeTransactions type;


}

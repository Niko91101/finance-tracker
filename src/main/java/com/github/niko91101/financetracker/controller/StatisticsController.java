package com.github.niko91101.financetracker.controller;

import com.github.niko91101.financetracker.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/balance/{userId}")
    public ResponseEntity<BigDecimal> getTotalTransactions(@PathVariable long userId) {
        return ResponseEntity.ok(statisticsService.getBalance(userId));
    }

    @GetMapping("/income/{userId}")
    public ResponseEntity<BigDecimal> getTotalIncome(@PathVariable long userId) {
        return ResponseEntity.ok(statisticsService.getTotalIncome(userId));
    }

    @GetMapping("/expense/{userId}")
    public ResponseEntity<BigDecimal> getTotalExpense(@PathVariable long userId) {
        return ResponseEntity.ok(statisticsService.getTotalExpense(userId));
    }
}

package com.github.niko91101.financetracker.controller;

import com.github.niko91101.financetracker.dto.response.CategoryStatisticsResponse;
import com.github.niko91101.financetracker.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/balance/{userId}")
    public ResponseEntity<BigDecimal> getTotalTransactions(@PathVariable Long userId) {
        return ResponseEntity.ok(statisticsService.getBalance(userId));
    }

    @GetMapping("/income/{userId}")
    public ResponseEntity<BigDecimal> getTotalIncome(@PathVariable Long userId) {
        return ResponseEntity.ok(statisticsService.getTotalIncome(userId));
    }

    @GetMapping("/expense/{userId}")
    public ResponseEntity<BigDecimal> getTotalExpense(@PathVariable Long userId) {
        return ResponseEntity.ok(statisticsService.getTotalExpense(userId));
    }

    @GetMapping("/users/{userId}/categories")
    public ResponseEntity<List<CategoryStatisticsResponse>> getStatisticsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(statisticsService.findStatisticsByUserId(userId));
    }
}

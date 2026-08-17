package com.github.niko91101.financetracker.specification;

import com.github.niko91101.financetracker.enums.TypeTransactions;
import com.github.niko91101.financetracker.model.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

public class TransactionSpecification {

    public static Specification<Transaction> hasUserId(Long userId) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Transaction> hasType(TypeTransactions type) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category").get("type"), type);
    }

    public static Specification<Transaction> hasMinAmount(BigDecimal minAmount) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), minAmount);
    }
}

package com.github.niko91101.financetracker.specification;

import com.github.niko91101.financetracker.enums.TypeTransactions;
import com.github.niko91101.financetracker.model.Transaction;
import org.springframework.data.jpa.domain.Specification;

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
}

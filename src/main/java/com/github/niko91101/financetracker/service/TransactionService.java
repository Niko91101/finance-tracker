package com.github.niko91101.financetracker.service;

import com.github.niko91101.financetracker.dto.request.CreateTransactionRequest;
import com.github.niko91101.financetracker.dto.request.UpdateTransactionRequest;
import com.github.niko91101.financetracker.dto.response.TransactionResponse;
import com.github.niko91101.financetracker.enums.TypeTransactions;
import com.github.niko91101.financetracker.exception.CategoryNotFoundException;
import com.github.niko91101.financetracker.exception.TransactionNotFoundException;
import com.github.niko91101.financetracker.mapper.TransactionMapper;
import com.github.niko91101.financetracker.model.Category;
import com.github.niko91101.financetracker.model.Transaction;
import com.github.niko91101.financetracker.model.User;
import com.github.niko91101.financetracker.repository.CategoryRepository;
import com.github.niko91101.financetracker.repository.TransactionRepository;
import com.github.niko91101.financetracker.repository.UserRepository;
import com.github.niko91101.financetracker.validation.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;

    @Transactional(readOnly = true)
    public List<TransactionResponse> getAllTransaction() {
        return transactionRepository.findAll()
                .stream()
                .map(transactionMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TransactionResponse getTransactionalById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));

        return transactionMapper.toResponse(transaction);
    }

    @Transactional
    public TransactionResponse saveTransaction(CreateTransactionRequest request) {

        ValidationUtil.validate(request);

        Transaction transaction = transactionMapper.toEntity(request);

        Category category = categoryRepository.findById(request.getCategoryId())
                        .orElseThrow(() -> new CategoryNotFoundException(request.getCategoryId()));

        User user = userRepository.findById(request.getUserId())
                        .orElseThrow(() -> new IllegalArgumentException("Пользователя с таким ID нет"));

        transaction.setCategory(category);
        transaction.setUser(user);

        return transactionMapper.toResponse(transactionRepository.save(transaction));
    }

    @Transactional
    public TransactionResponse updateTransaction(Long id, UpdateTransactionRequest request) {
        ValidationUtil.validate(request);

        if ((!transactionRepository.existsById(id))) {
            throw new IllegalArgumentException("Такой транзакции нет");
        }

        Transaction transaction = transactionMapper.toEntity(request);
        transaction.setId(id);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(request.getCategoryId()));

        transaction.setCategory(category);

        return transactionMapper.toResponse(transactionRepository.save(transaction));
    }

    @Transactional
    public void deleteTransaction(Long id) {

        ValidationUtil.validate(id);

        transactionRepository.deleteById(id);
    }

    public Integer sumTransactionsWithAmountMore500(List<Transaction> transactions) {
        ValidationUtil.validate(transactions);

        if (transactions.isEmpty()) {
            throw new IllegalArgumentException("Список транзацкий пуст");
        }

        return transactions.stream()
                .mapToInt(Transaction::getAmount)
                .filter(amount -> amount >= 500)
                .sum();
    }

    public Map<TypeTransactions, List<Transaction>> getTransactionOnCategory(List<Transaction> transactions) {
        return transactions.stream()
               .collect(Collectors.groupingBy((transaction ->  transaction.getCategory().getType())));
    }

    public Map<Long, Integer> getSumOfTransactionsByUserId(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(transaction -> transaction.getUser().getId(), Collectors.summingInt(Transaction::getAmount)));
    }
}

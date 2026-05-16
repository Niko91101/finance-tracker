package com.github.niko91101.financetracker.repository;

import com.github.niko91101.financetracker.IntegrationTestBase;
import com.github.niko91101.financetracker.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

public class UserRepositoryTest extends IntegrationTestBase {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Должен сохранить и найти пользователя по ID")
    @Transactional
    void shouldSaveAndFindUser() {
        User user = User.builder()
                .username("Стасик")
                .password("secret")
                .build();

        User saved = userRepository.save(user);

        Optional<User> found = userRepository.findById(saved.getId());

        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("Стасик", found.get().getUsername());
    }

    @Test
    @Transactional
    @DisplayName("Должен вернуть пустой Optional когда пользователь не найден")
    void shouldReturnEmptyWhenUserNotFound()
    {
        Optional<User> found = userRepository.findById(999L);
        Assertions.assertTrue(found.isEmpty());
    }}

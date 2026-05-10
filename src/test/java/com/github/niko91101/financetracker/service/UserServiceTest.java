package com.github.niko91101.financetracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;

@DisplayName("UserService - null валидация")
public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(null, null);
    }

    @Test
    @DisplayName("getUserById : должен выбросить исключение при null id")
    void shouldThrowWhenGetUserByIdCalledWithNull() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.getUserById(null));
    }

    @Test
    @DisplayName("saveUser : должен выбросить исключение при null запросе")
    void shouldThrowWhenSaveUserCalledWithNull() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.saveUser(null));
    }

    @Test
    @DisplayName("upgradeUser : должен выбросить исключение при null запросе")
    void shouldThrowWhenUpdateUserCalledWithNull() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(1L, null));
    }

    @Test
    @DisplayName("deleteUser: должен выбросить исключение при null id")
    void shouldThrowWhenDeleteUserCalledWithNull() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.deleteUser(null));
    }
}

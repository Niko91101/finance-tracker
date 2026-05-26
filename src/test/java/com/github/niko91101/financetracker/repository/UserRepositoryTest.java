package com.github.niko91101.financetracker.repository;

import com.github.niko91101.financetracker.IntegrationTestBase;
import com.github.niko91101.financetracker.model.User;
import com.github.niko91101.financetracker.util.UserTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest extends IntegrationTestBase {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Должен сохранить и найти пользователя по ID")
    void shouldSaveAndFindUser() {
        User user = UserTestFactory.createDefaultUser();

        User saved = userRepository.save(user);

        Optional<User> found = userRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        Assertions.assertEquals("Стасик", found.get().getUsername());
    }

    @Test
    @DisplayName("Должен удалить пользователя по id")
    void shouldDeleteUser() {
        User user = UserTestFactory.createDefaultUser();

        User saved = userRepository.save(user);
        userRepository.deleteById(saved.getId());

        Optional<User> found = userRepository.findById(saved.getId());
        assertTrue(found.isEmpty());
    }


    @Test
    @DisplayName("Должен вернуть пустой Optional когда пользователь не найден")
    void shouldReturnEmptyWhenUserNotFound() {
        Optional<User> found = userRepository.findById(999L);
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Должен обновить данные пользователя")
    void shouldUpdateUser() {
        User user = UserTestFactory.createDefaultUser();

        User saved = userRepository.save(user);

        saved.setUsername("Стас");
        userRepository.save(saved);

        Optional<User> found = userRepository.findById(saved.getId());

        assertEquals("Стас", found.get().getUsername());
    }

    @Test
    @DisplayName("Должен вернуть список всех пользователей")
    void shouldFindAllUsers() {
        User user1 = UserTestFactory.createDefaultUser();

        User user2 = UserTestFactory.createUser("Павел");

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> found = userRepository.findAll();

        assertEquals(2, found.size());
    }
}





package com.github.niko91101.financetracker.service;

import com.github.niko91101.financetracker.dto.response.UserResponse;
import com.github.niko91101.financetracker.mapper.UserMapper;
import com.github.niko91101.financetracker.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.github.niko91101.financetracker.model.User;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService - null валидация")
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserService userService;

    private User userEntity;
    private UserResponse userResponse;


    @BeforeEach
    void setUp() {
        userEntity = User.builder()
                .id(1L)
                .username("Стас")
                .password("secret")
                .build();

        userResponse = UserResponse.builder()
                .id(1L)
                .username("Стас")
                .build();
    }

    @Nested
    @DisplayName("getUserById")
    class GetUserById {

        @Test
        @DisplayName("Должен вернуть UserResponse когда пользователь найден")
        void shouldReturnUserResponseWhenFound() {
            when(userRepository.findById(1L))
                    .thenReturn(Optional.of(userEntity));

            when(userMapper.toResponse(userEntity))
                    .thenReturn(userResponse);

            UserResponse result = userService.getUserById(1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Стас", result.getUsername());

            verify(userRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Должен выбросить исключение IllegalArgumentException, когда пользователь не найден")
        void shouldThrowExceptionWhenNotFound() {
            when(userRepository.findById(99L))
                    .thenReturn(Optional.empty());

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> userService.getUserById(99L));

            assertTrue(ex.getMessage().contains("99"));
            verify(userRepository).findById(99L);

            verifyNoInteractions(userMapper);
        }

        @Test
        @DisplayName("Должен выбросить IllegalArgumentException когда передан null вместно id")
        void shouldThrowExceptionWhenIdIsNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> userService.getUserById(null));

            verifyNoInteractions(userMapper, userRepository);
        }
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

package com.github.niko91101.financetracker.service;

import com.github.niko91101.financetracker.dto.request.CreateUserRequest;
import com.github.niko91101.financetracker.dto.request.UpdateUserRequest;
import com.github.niko91101.financetracker.dto.response.UserResponse;
import com.github.niko91101.financetracker.exception.UserNotFoundException;
import com.github.niko91101.financetracker.mapper.UserMapper;
import com.github.niko91101.financetracker.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import com.github.niko91101.financetracker.model.User;

import java.util.Optional;

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
        @DisplayName("Должен выбросить исключение UserNotFoundException, когда пользователь не найден")
        void shouldThrowExceptionWhenNotFound() {
            when(userRepository.findById(99L))
                    .thenReturn(Optional.empty());

            UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                    () -> userService.getUserById(99L));

            assertTrue(ex.getMessage().contains("99"));
            verify(userRepository).findById(99L);

            verifyNoInteractions(userMapper);
        }

        @Test
        @DisplayName("Должен выбросить IllegalArgumentException когда передан null вместо id")
        void shouldThrowExceptionWhenIdIsNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> userService.getUserById(null));

            verifyNoInteractions(userMapper, userRepository);
        }
    }


    @Nested
    @DisplayName("saveUser")
    class SaveUser {

        CreateUserRequest request;

        @BeforeEach
        void setup() {
            request = CreateUserRequest.builder()
                    .username("Стас")
                    .password("secret")
                    .build();
        }

        @Test
        @DisplayName("Должен сохранить пользователя и вернуть UserResponse")
        void shouldSaveUser() {
            when(userMapper.toEntity(request))
                    .thenReturn(userEntity);

            when(userRepository.save(userEntity))
                    .thenReturn(userEntity);

            when(userMapper.toResponse(userEntity))
                    .thenReturn(userResponse);

            UserResponse result = userService.saveUser(request);

            assertNotNull(result);

            InOrder inOrder = inOrder(userMapper, userRepository);
            inOrder.verify(userMapper).toEntity(request);
            inOrder.verify(userRepository).save(userEntity);
            inOrder.verify(userMapper).toResponse(userEntity);
            inOrder.verifyNoMoreInteractions();
        }

        @Test
        @DisplayName("Должен выбросить исключение IllegalArgumentException, когда request null")
        void shouldThrowWhenRequestIsNull() {

            assertThrows(IllegalArgumentException.class,
                    () -> userService.saveUser(null));

            verifyNoInteractions(userRepository, userMapper);
        }
    }

    @Nested
    class UpdateUser {
        UpdateUserRequest request;

        @BeforeEach
        void setup() {
            request = UpdateUserRequest.builder()
                    .username("Стасик")
                    .password("newSecret")
                    .build();
        }

        @Test
        @DisplayName("Должен обновить пользователя пользователя без вызова save")
        void shouldUpdateUser() {

            when(userRepository.findById(1L))
                    .thenReturn(Optional.of(userEntity));

            when(userMapper.toResponse(userEntity))
                    .thenReturn(userResponse);


            UserResponse result = userService.updateUser(1L, request);
            assertEquals("newSecret", userEntity.getPassword());
            assertEquals("Стасик", userEntity.getUsername());

            verify(userRepository).findById(1L);
            verify(userRepository, never()).save(any());
            verify(userMapper).toResponse(userEntity);

            assertSame(userResponse, result);
        }

        @Test
        @DisplayName("Должен выбросить исключения IllegalArgumentException когда пользователь не найден")
        void shouldThrowWhenUserNotFound() {
            when(userRepository.findById(99L))
                    .thenReturn(Optional.empty());

            UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                    () -> userService.updateUser(99L, request));

            assertTrue(ex.getMessage().contains("99"));
            verify(userRepository).findById(99L);
            verify(userRepository, never()).save(any());
            verifyNoInteractions(userMapper);
        }
    }

    @Nested
    @DisplayName("getUserById() - валидация id")
    class GetUserByIdValidation {

        @ParameterizedTest
        @ValueSource(longs = {0L, -1L, -100L})
        @NullSource
        @DisplayName("Должен выбросить исключение когда значение отрицательное или ноль")
        void shouldThrowWhenIdNotPositive(Long id) {

            assertThrows(IllegalArgumentException.class,
                    () -> userService.getUserById(id));

            verifyNoInteractions(userRepository, userMapper);
        }
    }
}

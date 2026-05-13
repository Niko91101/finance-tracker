package com.github.niko91101.financetracker.service;

import com.github.niko91101.financetracker.dto.request.CreateUserRequest;
import com.github.niko91101.financetracker.dto.request.UpdateUserRequest;
import com.github.niko91101.financetracker.dto.response.UserResponse;
import com.github.niko91101.financetracker.mapper.UserMapper;
import com.github.niko91101.financetracker.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
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
    @DisplayName("updateUser")
    class UpdateUser {
        UpdateUserRequest request;

        @BeforeEach
        void setup() {
            request = UpdateUserRequest.builder()
                    .username("Стасик")
                    .password("secret")
                    .build();
        }

        @Test
        @DisplayName("Должен изменить пользователя и вернуть UserResponse")
        void shouldUpdateUser() {

            when(userRepository.existsById(1L))
                    .thenReturn(true);
            when(userMapper.toEntity(request))
                    .thenReturn(userEntity);

            when(userRepository.save(any()))
                    .thenReturn(userEntity);

            when(userMapper.toResponse(userEntity))
                    .thenReturn(userResponse);


            UserResponse result = userService.updateUser(1L, request);

            assertNotNull(result);

            ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
            verify(userRepository.save(captor.capture()));

            assertEquals(1L, captor.getValue().getId());
        }
    }
}

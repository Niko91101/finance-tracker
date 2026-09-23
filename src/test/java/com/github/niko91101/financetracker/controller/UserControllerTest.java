package com.github.niko91101.financetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.niko91101.financetracker.dto.request.CreateUserRequest;
import com.github.niko91101.financetracker.dto.request.UpdateUserRequest;
import com.github.niko91101.financetracker.dto.response.UserResponse;
import com.github.niko91101.financetracker.exception.UserNotFoundException;
import com.github.niko91101.financetracker.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(UserController.class)
@ImportAutoConfiguration(JacksonAutoConfiguration.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    UserService userService;

    @Test
    void shouldReturnUserById() throws Exception {
        UserResponse response = UserResponse.builder()
                .id(1L)
                .username("Стасик")
                .build();

        when(userService.getUserById(1L)).thenReturn(response);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("Стасик"))
                .andDo(print());
    }

    @Test
    void shouldReturnBadRequestWhenCreatedUserRequestIsInvalid() throws Exception {

        CreateUserRequest request = CreateUserRequest.builder()
                .username("")
                .password("")
                .build();
        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Ошибка валидации"))
                .andExpect(jsonPath("$.method").value("POST"))
                .andExpect(jsonPath("$.path").value("/users"))
                .andExpect(jsonPath("$.fieldErrors.username").exists())
                .andExpect(jsonPath("$.fieldErrors.password").exists());

        verify(userService, never()).saveUser(any(CreateUserRequest.class));
    }

    @Test
    @DisplayName(value = "Должен вернуть код 400 при некорректном ID")
    void shouldReturnBadRequestWhenGetUserRequestIsInvalid() throws Exception {

        mockMvc.perform(get("/users/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.method").value("GET"))
                .andExpect(jsonPath("$.path").value("/users/abc"))
                .andExpect(jsonPath("$.message")
                        .value("Некорректное значение параметра: id"));
    }

    @Test
    @DisplayName(value = "Должен вернуть ApiError при отсутствии пользователя")
    void shouldReturnApiErrorWhenUserNotFound() throws Exception {

        when(userService.getUserById(99L))
                .thenThrow(new UserNotFoundException(99L));

        mockMvc.perform(get("/users/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.method").value("GET"))
                .andExpect(jsonPath("$.path").value("/users/99"))
                .andExpect(jsonPath("$.message").value("Пользователь с ID: 99 не найден"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnInternalServerError() throws Exception {
        when(userService.getUserById(99L))
                .thenThrow(new RuntimeException("Пароль базы данных: secret"));

        mockMvc.perform(get("/users/{id}", 99L))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Внутренняя ошибка сервера"))
                .andExpect(jsonPath("$.method").value("GET"))
                .andExpect(jsonPath("$.path").value("/users/99"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());
    }

    @Test
    @DisplayName(value = "Должен вернуть 400 при некорректных данных при UpdateUser")
    void shouldReturnBadRequestWhenUpdateUserRequestIsInvalid() throws Exception {

        UpdateUserRequest request = UpdateUserRequest.builder()
                .username("")
                .password("")
                .build();

        mockMvc.perform(
                        put("/users/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUser(eq(1L), any(UpdateUserRequest.class));
    }

    @Test
    @DisplayName(value = "Должен вернуть 400, когда JSON некорректно сформирован")
    void shouldReturnBadRequestWhenJsonIsMalformed() throws Exception {

        mockMvc.perform(
                put("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "Стасик",
                                  "password": "secret"
                                """)
        )
                .andExpect(status().isBadRequest());

        verify(userService, never())
                .updateUser(anyLong(), any(UpdateUserRequest.class));
    }
}

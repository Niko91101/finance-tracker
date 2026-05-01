package com.github.niko91101.financetracker.mapper;

import com.github.niko91101.financetracker.dto.request.CreateUserRequest;
import com.github.niko91101.financetracker.dto.request.UpdateUserRequest;
import com.github.niko91101.financetracker.dto.response.UserResponse;
import com.github.niko91101.financetracker.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .date(user.getDate())
                .build();
    }

    public User toEntity(CreateUserRequest createUserRequest) {
        return User.builder()
                .username(createUserRequest.getUsername())
                .password(createUserRequest.getPassword())
                .build();
    }

    public User toEntity(UpdateUserRequest updateUserRequest) {
        return User.builder()
                .username(updateUserRequest.getUsername())
                .password(updateUserRequest.getPassword())
                .build();
    }
}

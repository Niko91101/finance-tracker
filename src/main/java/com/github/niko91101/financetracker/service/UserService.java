package com.github.niko91101.financetracker.service;

import com.github.niko91101.financetracker.dto.request.CreateUserRequest;
import com.github.niko91101.financetracker.dto.request.UpdateUserRequest;
import com.github.niko91101.financetracker.dto.response.UserResponse;
import com.github.niko91101.financetracker.mapper.UserMapper;
import com.github.niko91101.financetracker.model.User;
import com.github.niko91101.financetracker.repository.UserRepository;
import com.github.niko91101.financetracker.validation.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse getUserById(Long id) {
        ValidationUtil.validate(id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователя с  id: " + id + " не найдено!" ));

        return userMapper.toResponse(user);
    }

    public UserResponse saveUser(CreateUserRequest user) {
        ValidationUtil.validate(user);

        User userEntity = userRepository.save(userMapper.toEntity(user));
        return userMapper.toResponse(userEntity);
    }

    public UserResponse updateUser(Long id, UpdateUserRequest updateUser) {
        ValidationUtil.validate(updateUser);

        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Пользователя с id: " + id + " нет");
        }

        User updatedUser = userMapper.toEntity(updateUser);
        updatedUser.setId(id);
        User savedUser = userRepository.save(updatedUser);

        return userMapper.toResponse(savedUser);
    }

    public void deleteUser(Long id) {
        ValidationUtil.validate(id);
        userRepository.deleteById(id);
    }

}

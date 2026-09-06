package com.github.niko91101.financetracker.service;

import com.github.niko91101.financetracker.dto.request.CreateUserRequest;
import com.github.niko91101.financetracker.dto.request.UpdateUserRequest;
import com.github.niko91101.financetracker.dto.response.UserResponse;
import com.github.niko91101.financetracker.exception.UserNotFoundException;
import com.github.niko91101.financetracker.mapper.UserMapper;
import com.github.niko91101.financetracker.model.User;
import com.github.niko91101.financetracker.repository.UserRepository;
import com.github.niko91101.financetracker.validation.ValidationUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        ValidationUtil.validate(id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse saveUser(CreateUserRequest user) {
        ValidationUtil.validate(user);

        User userEntity = userRepository.save(userMapper.toEntity(user));
        return userMapper.toResponse(userEntity);
    }

    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest updateUser) {
        ValidationUtil.validate(updateUser);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setUsername(updateUser.getUsername());
        user.setPassword(updateUser.getPassword());

        return userMapper.toResponse(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        ValidationUtil.validate(id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.delete(user);
    }
}

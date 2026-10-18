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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

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

        User userEntity = userMapper.toEntity(user);
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        userEntity.setPassword(encodedPassword);
        User savedUser = userRepository.save(userEntity);

        return userMapper.toResponse(savedUser);
    }

    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest updateUser) {
        ValidationUtil.validate(updateUser);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        String encodedPassword = passwordEncoder.encode(updateUser.getPassword());
        user.setUsername(updateUser.getUsername());
        user.setPassword(encodedPassword);

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

package com.github.niko91101.financetracker.service;

import com.github.niko91101.financetracker.model.User;
import com.github.niko91101.financetracker.repository.TransactionRepository;
import com.github.niko91101.financetracker.repository.UserRepository;
import com.github.niko91101.financetracker.validation.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public User getUserById(Long id) {
        ValidationUtil.validate(id);

        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователя с  id: " + id + " не найдено!" ));
    }

    public User saveUser(User user) {
        ValidationUtil.validate(user);

        return userRepository.save(user);
    }

    public User updateUser(Long id, User updateUser) {
        ValidationUtil.validate(updateUser);

        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Пользователя с id: " + id + " нет");
        }

        updateUser.setId(id);

        return userRepository.save(updateUser);

    }

    public void deleteUser(Long id) {
        ValidationUtil.validate(id);
        userRepository.deleteById(id);
    }

}

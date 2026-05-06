package com.github.niko91101.financetracker.service;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class User {
    String name;
    int age;



    public List<String> filter(List<User> users) {
        return users.stream()
                .filter(user -> user.age > 18)
                .map(User::getName)
                .distinct()
                .sorted()
                .toList();
    }
}

package com.github.niko91101.financetracker.util;

import com.github.niko91101.financetracker.model.User;

public class UserTestFactory {

    public static User createUser(String username) {

        return User.builder()
                .username(username)
                .password("secret")
                .build();
    }

    public static User createDefaultUser() {
        return createUser("Стасик");
    }
}

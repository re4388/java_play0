package com.ben.sprintbootexceldemo.service;

import com.ben.sprintbootexceldemo.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    public List<User> findAllUsers() {
        return List.of(
                new User(1L, "Tom", "tom@test.com"),
                new User(2L, "Mary", "mary@test.com")
        );
    }
}


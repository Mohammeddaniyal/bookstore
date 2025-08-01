package com.daniyal.bookstore.service;

import com.daniyal.bookstore.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> getAllUsers();
    Optional<User> updateUser(Long id,User user);
}

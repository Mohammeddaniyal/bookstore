package com.daniyal.bookstore.service;

import com.daniyal.bookstore.dto.LoginRequestDTO;
import com.daniyal.bookstore.entity.User;
import com.daniyal.bookstore.exceptions.InvalidCredentialsException;
import com.daniyal.bookstore.exceptions.UserAlreadyExistsException;
import com.daniyal.bookstore.exceptions.UserNotExistsException;
import com.daniyal.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public User registerUser(User user) {

        Map<String,String> errorMap =new HashMap<>();
        Optional<User> userOptional=userRepository.findByEmail(user.getEmail());
        if(userOptional.isPresent())
        {
            errorMap.put("email","Email already exists");
        }
        if(userRepository.findByUsername(user.getUsername()).isPresent())
        {
            errorMap.put("user","User already exists");
        }
        if(!errorMap.isEmpty())
        {
            throw new UserAlreadyExistsException(errorMap);
        }
        user.setRoles(Arrays.asList("CUSTOMER"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public String login(LoginRequestDTO loginRequest) {
        Optional<User> userOptional=userRepository.findByEmail(loginRequest.getEmail());
        if(userOptional.isPresent())
        {
            User userDb=userOptional.get();
            if(!passwordEncoder.matches(loginRequest.getPassword(),userDb.getPassword()))
            {
                throw new InvalidCredentialsException("Invalid password");
            }
            return jwtUtil.generateToken(userDb.getUsername(),userDb.getRoles());
        }
        throw new InvalidCredentialsException("Invalid email");
    }

}

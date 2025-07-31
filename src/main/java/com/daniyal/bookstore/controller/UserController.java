package com.daniyal.bookstore.controller;

import com.daniyal.bookstore.dto.UserRequestDTO;
import com.daniyal.bookstore.dto.UserResponseDTO;
import com.daniyal.bookstore.entity.User;
import com.daniyal.bookstore.service.UserService;
import com.daniyal.bookstore.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRequestDTO userRequest)
    {
        User user=User.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .build();
        // register the user
        User savedUser=userService.registerUser(user);
        if(savedUser!=null) {
            UserResponseDTO userResponse = UserResponseDTO.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .id(user.getId())
                    .roles(user.getRoles())
                    .build();
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }
       return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username)
    {
        Optional<User> userOptional=userService.findByUsername(username);
        if(userOptional.isPresent())
        {
            User user=userOptional.get();
            UserResponseDTO userResponse=UserResponseDTO.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .id(user.getId())
                    .roles(user.getRoles())
                    .build();
            return new ResponseEntity<>(userResponse,HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}

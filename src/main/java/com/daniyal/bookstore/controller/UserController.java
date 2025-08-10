package com.daniyal.bookstore.controller;

import com.daniyal.bookstore.dto.LoginRequestDTO;
import com.daniyal.bookstore.dto.LoginResponseDTO;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@Valid @RequestBody LoginRequestDTO loginRequest)
    {
        String token=userService.login(loginRequest);

        LoginResponseDTO loginResponse=new LoginResponseDTO("Login Successful",token);
        return new ResponseEntity<>(loginResponse,HttpStatus.OK);
    }


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
            return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
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
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll()
    {
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }

}

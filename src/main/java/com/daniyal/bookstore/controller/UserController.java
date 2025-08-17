package com.daniyal.bookstore.controller;

import com.daniyal.bookstore.dto.LoginRequestDTO;
import com.daniyal.bookstore.dto.LoginResponseDTO;
import com.daniyal.bookstore.dto.UserRequestDTO;
import com.daniyal.bookstore.dto.UserResponseDTO;
import com.daniyal.bookstore.entity.User;
import com.daniyal.bookstore.exceptions.ApiErrorResponse;
import com.daniyal.bookstore.service.UserService;
import com.daniyal.bookstore.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@Tag(name = "User", description = "APIs related to user registration, login and retrieval")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid login request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@Valid @RequestBody LoginRequestDTO loginRequest)
    {
        String token=userService.login(loginRequest);

        LoginResponseDTO loginResponse=new LoginResponseDTO("Login Successful",token);
        return new ResponseEntity<>(loginResponse,HttpStatus.OK);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid login request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
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

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
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

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of users retrieved",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll()
    {
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }

}

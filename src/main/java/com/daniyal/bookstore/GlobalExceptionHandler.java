package com.daniyal.bookstore;

import com.daniyal.bookstore.exceptions.ApiErrorResponse;
import com.daniyal.bookstore.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationExceptions(MethodArgumentNotValidException validException)
    {
        Map<String,String> errors=new HashMap<>();

        validException.getBindingResult().getAllErrors().forEach(error->{
            String fieldName=((FieldError)error).getField();
            String message=error.getDefaultMessage();
            errors.put(fieldName,message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException exception)
    {
        ApiErrorResponse apiErrorResponse=new ApiErrorResponse();
        apiErrorResponse.setMessage("User registration failed due to duplicate entries");
        apiErrorResponse.setErrorCode("USER_ALREADY_EXISTS");
        apiErrorResponse.setErrors(exception.getErrorMessages());
        return new ResponseEntity<>(apiErrorResponse,HttpStatus.BAD_REQUEST);
    }
}

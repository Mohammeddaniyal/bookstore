package com.daniyal.bookstore;

import com.daniyal.bookstore.exceptions.*;
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
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException validException)
    {
        Map<String,String> errors=new HashMap<>();

        validException.getBindingResult().getAllErrors().forEach(error->{
            String fieldName=((FieldError)error).getField();
            String message=error.getDefaultMessage();
            errors.put(fieldName,message);
        });

        ApiErrorResponse apiErrorResponse=new ApiErrorResponse();
        apiErrorResponse.setMessage("Validation failed for one or more fields");
        apiErrorResponse.setErrorCode("VALIDATION_ERROR");
        apiErrorResponse.setErrors(errors);

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException exception)
    {
        ApiErrorResponse apiErrorResponse=new ApiErrorResponse();
        apiErrorResponse.setMessage("User registration failed due to duplicate entries");
        apiErrorResponse.setErrorCode("USER_ALREADY_EXISTS");
        apiErrorResponse.setErrors(exception.getErrorMap());
        return new ResponseEntity<>(apiErrorResponse,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException exception)
    {
        return new ResponseEntity<>(ApiErrorResponse.builder()
                .message(exception.getMessage())
                .errorCode("INVALID_CREDENTIALS")
                .errors(new HashMap<>())
                .build(),HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(BookAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleBookAlreadyExistsException(BookAlreadyExistsException exception)
    {
        return new ResponseEntity<>(ApiErrorResponse.builder()
                .message(exception.getMessage())
                .errorCode("BOOK_ALREADY_EXISTS")
                .errors(new HashMap<>())
                .build(),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(BookNotExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleBookNotExistsException(BookNotExistsException exception)
    {
        return new ResponseEntity<>(ApiErrorResponse.builder()
                .message(exception.getMessage())
                .errorCode("BOOK_NOT_EXISTS")
                .errors(new HashMap<>())
                .build(),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DataPersistenceException.class)
    public ResponseEntity<ApiErrorResponse> handleDataPersistenceException(DataPersistenceException exception)
    {
        return new ResponseEntity<>(ApiErrorResponse.builder()
                .message(exception.getMessage())
                .errorCode("DATA_PERSISTENCE_ERROR")
                .errors(new HashMap<>())
                .build(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

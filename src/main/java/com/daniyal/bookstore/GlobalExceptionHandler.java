package com.daniyal.bookstore;

import com.daniyal.bookstore.entity.Author;
import com.daniyal.bookstore.exceptions.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.PersistenceException;
import org.hibernate.HibernateException;
import org.hibernate.TypeMismatchException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Collections;
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
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleBookNotFoundException(BookNotFoundException exception)
    {
        return new ResponseEntity<>(ApiErrorResponse.builder()
                .message(exception.getMessage())
                .errorCode("BOOK_NOT_FOUND")
                .errors(new HashMap<>())
                .build(),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({DataAccessException.class, PersistenceException.class, HibernateException.class})
    public ResponseEntity<ApiErrorResponse> handleDataPersistenceException(Exception exception)
    {
        return new ResponseEntity<>(ApiErrorResponse.builder()
                .message("A data persistence exception occured")
                .errorCode("DATA_PERSISTENCE_ERROR")
                .errors(new HashMap<>())
                .build(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(ImmutableFieldException.class)
    public ResponseEntity<ApiErrorResponse> handleImmutableFieldException(ImmutableFieldException exception)
    {
        return new ResponseEntity<>(ApiErrorResponse.builder()
                .message(exception.getMessage())
                .errorCode("IMMUTABLE_FIELD_ERROR")
                .errors(new HashMap<>())
                .build(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AuthorAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthorAlreadyExistsException(AuthorAlreadyExistsException exception)
    {
        return new ResponseEntity<>(ApiErrorResponse.builder()
                .message(exception.getMessage())
                .errorCode("AUTHOR_ALREADY_EXISTS")
                .errors(new HashMap<>())
                .build(),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthorNotFoundException(AuthorNotFoundException exception)
    {
        return new ResponseEntity<>(ApiErrorResponse.builder()
                .message(exception.getMessage())
                .errorCode("AUTHOR_NOT_FOUND")
                .errors(new HashMap<>())
                .build(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(ApiErrorResponse.builder()
                .message("Malformed JSON request")
                .errorCode("MALFORMED_JSON_REQUEST")
                .errors(new HashMap<>())
                .build(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({ MethodArgumentTypeMismatchException.class, TypeMismatchException.class, MissingServletRequestParameterException.class })
    public ResponseEntity<ApiErrorResponse> handleBadRequestParams(Exception ex) {
        String errorMessage;
        if (ex instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException e = (MissingServletRequestParameterException) ex;
            errorMessage = "Required query parameter '" + e.getParameterName() + "' is missing";
        } else if (ex instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException e = (MethodArgumentTypeMismatchException) ex;
            errorMessage = "Parameter '" + e.getName() + "' should be of type " + e.getRequiredType().getSimpleName();
        } else {
            errorMessage = "Invalid query parameter";
        }
        return new ResponseEntity<>(ApiErrorResponse.builder()
                .message(errorMessage)
                .errorCode("ERR_INVALID_QUERY_PARAM")
                .errors(new HashMap<>())
                .build(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        String errorMessage = ex.getMessage() != null ? ex.getMessage() : "Invalid argument provided";

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .message(errorMessage)
                .errorCode("ERR_ILLEGAL_ARGUMENT")
                .errors(new HashMap<>())  // or empty map/list if preferred
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiErrorResponse> handleExpiredJwt(ExpiredJwtException ex) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .errorCode("TOKEN_EXPIRED")
                .message("JWT token has expired")
                .errors(Collections.emptyMap())
                .build();

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiErrorResponse> handleSignatureException(SignatureException ex) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .errorCode("INVALID_TOKEN_SIGNATURE")
                .message("JWT token signature is invalid")
                .errors(Collections.emptyMap())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiErrorResponse> handleMalformedJwtException(MalformedJwtException ex) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .errorCode("MALFORMED_TOKEN")
                .message("JWT token is malformed")
                .errors(Collections.emptyMap())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<ApiErrorResponse> handleUnsupportedJwtException(UnsupportedJwtException ex) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .errorCode("UNSUPPORTED_TOKEN")
                .message("JWT token is unsupported")
                .errors(Collections.emptyMap())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(NegativeArraySizeException.class)
    public ResponseEntity<ApiErrorResponse> handleNegativeArraySizeException(NegativeArraySizeException ex) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .errorCode("MALFORMED_TOKEN")
                .message("JWT token is malformed or improperly encoded")
                .errors(Collections.emptyMap())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DecodingException.class)
    public ResponseEntity<ApiErrorResponse> handleDecodingException(DecodingException ex) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .errorCode("MALFORMED_TOKEN")
                .message("JWT token is malformed or improperly encoded")
                .errors(Collections.emptyMap())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(OrderOutOfStockException.class)
    public ResponseEntity<ApiErrorResponse> handleOrderOutOfStockException(OrderOutOfStockException ex) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .errorCode("ORDER_OUT_OF_STOCK")
                .message(ex.getMessage())
                .errors(Collections.emptyMap())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleOrderNotFoundException(OrderNotFoundException ex) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .errorCode("ORDER_NOT_FOUND")
                .message(ex.getMessage())
                .errors(Collections.emptyMap())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .errorCode("ACCESS_DENIED_EXCEPTION")
                .message(ex.getMessage())
                .errors(Collections.emptyMap())
                .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }


//        @ExceptionHandler(Exception.class)
//        public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
//
//            String errorCode = "INTERNAL_ERROR";
//            String message = "An unexpected error occurred";
//
//            // Special case: handle NegativeArraySizeException inside
//            if (ex instanceof NegativeArraySizeException) {
//                errorCode = "MALFORMED_TOKEN";
//                message = "JWT token is malformed or improperly encoded";
//            }
//
//            System.out.println("Exception class: " + ex.getClass().getName());
//
//            System.out.println("Exception class (simple): " + ex.getClass().getSimpleName());
//
//            ApiErrorResponse error = ApiErrorResponse.builder()
//                    .errorCode(errorCode)
//                    .message(message)
//                    .errors(Collections.emptyMap())
//                    .build();
//
//            // JWT/security related cases should return 401 instead:
//            HttpStatus status = (ex instanceof NegativeArraySizeException)
//                    ? HttpStatus.UNAUTHORIZED
//                    : HttpStatus.INTERNAL_SERVER_ERROR;
//
//            return new ResponseEntity<>(error, status);
//        }



}

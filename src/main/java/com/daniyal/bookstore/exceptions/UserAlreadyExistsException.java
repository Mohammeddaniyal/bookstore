package com.daniyal.bookstore.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Map;
@Getter

public class UserAlreadyExistsException extends RuntimeException{

    private final Map<String,String> errorMap;

    public UserAlreadyExistsException(Map<String,String> errorMap)
    {
        this.errorMap= Collections.unmodifiableMap(errorMap);
    }

}

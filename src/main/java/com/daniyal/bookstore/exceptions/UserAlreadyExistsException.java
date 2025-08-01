package com.daniyal.bookstore.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Getter

public class UserAlreadyExistsException extends RuntimeException{

    private final List<String> errorMessages;

    public UserAlreadyExistsException(List<String> errorMessages)
    {
        this.errorMessages= Collections.unmodifiableList(errorMessages);
    }

}

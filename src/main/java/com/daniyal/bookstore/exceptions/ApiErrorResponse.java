package com.daniyal.bookstore.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class ApiErrorResponse {
    private String message;
    private String errorCode;
    private Map<String,String> errors;
}

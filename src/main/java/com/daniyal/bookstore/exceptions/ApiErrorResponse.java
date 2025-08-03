package com.daniyal.bookstore.exceptions;

import lombok.*;

import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiErrorResponse {
    private String message;
    private String errorCode;
    private Map<String,String> errors;
}

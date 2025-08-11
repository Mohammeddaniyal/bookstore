package com.daniyal.bookstore.security.handlers;
import com.daniyal.bookstore.exceptions.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        logger.warn("Authentication failure: {}", authException.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String errorCode = "ERR_AUTH_UNAUTHORIZED";
        String message = "Authentication is required";

        Throwable cause = authException.getCause();
        // Check for JWT-specific exceptions and set error code/message accordingly
        if (cause != null) {
            String causeClassName = cause.getClass().getSimpleName();

            switch (causeClassName) {
                case "ExpiredJwtException":
                    errorCode = "TOKEN_EXPIRED";
                    message = "JWT token has expired";
                    break;

                case "SignatureException":
                    errorCode = "INVALID_TOKEN_SIGNATURE";
                    message = "JWT token signature is invalid";
                    break;

                case "MalformedJwtException":
                    errorCode = "MALFORMED_TOKEN";
                    message = "JWT token is malformed";
                    break;

                case "UnsupportedJwtException":
                    errorCode = "UNSUPPORTED_TOKEN";
                    message = "JWT token is unsupported";
                    break;

                case "IllegalArgumentException":
                    errorCode = "INVALID_TOKEN";
                    message = "JWT token is invalid or missing";
                    break;

                // Add other JWT related exceptions here as desired

                default:
                    // Leave default errorCode and message
                    break;
            }
        }else {
            String exMsg = authException.getMessage() != null ? authException.getMessage().toLowerCase() : "";
            if (exMsg.contains("expired")) {
                errorCode = "TOKEN_EXPIRED";
                message = "JWT token has expired";
            } else if (exMsg.contains("signature")) {
                errorCode = "INVALID_TOKEN_SIGNATURE";
                message = "JWT token signature is invalid";
            } else if (exMsg.contains("full authentication is required")) {
                errorCode = "AUTH_REQUIRED";
                message = "Full authentication is required to access this resource";
            }
        }

        ApiErrorResponse error = ApiErrorResponse.builder()
                .errorCode(errorCode)
                .message(message)
                .errors(new HashMap<>())
                .build();

        // Use a reusable ObjectMapper or create new
        new ObjectMapper().writeValue(response.getWriter(), error);
    }

}

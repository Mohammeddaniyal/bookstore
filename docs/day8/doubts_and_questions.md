1. **Detecting Specific JWT Exceptions in Wrapped Exceptions**
    - How to identify which JWT exception occurred when exceptions like `ExpiredJwtException` or `MalformedJwtException` are wrapped inside Spring Security’s `BadCredentialsException` or other wrappers?
    - Solution: Inspect the cause inside the custom `AuthenticationEntryPoint` or delegate to a global exception handler that unwraps the cause.

2. **Why Original Exception Cause Gets Lost**
    - Why does the cause often become `null` or lost when the JWT exception reaches the `CustomAuthenticationEntryPoint`?
    - Explained: This is due to Spring Security’s internal exception wrapping and filtering flow, which can swallow or lose the original cause.

3. **Effectiveness of Global Exception Handlers for JWT in Filters**
    - Why global `@RestControllerAdvice` exception handlers do not catch JWT exceptions thrown inside the security filter by default?
    - Clarification: Security filters catch exceptions before they reach controller layers, so extra mechanisms are needed to delegate these exceptions properly.

4. **Using `HandlerExceptionResolver` in the JWT Filter**
    - How and why injecting and using `HandlerExceptionResolver` inside the JWT filter helps forward JWT exceptions to the global exception handler?
    - Benefit: It delegates exceptions into Spring MVC’s exception handling mechanism, allowing consistent, centralized JSON error responses for JWT exceptions.

5. **Handling Specific Low-Level Exceptions like `NegativeArraySizeException` and `DecodingException`**
    - Why do some malformed tokens cause low-level exceptions like `NegativeArraySizeException` or library exceptions like `DecodingException`?
    - How to catch and handle these properly to avoid 500 Internal Server Errors and instead return user-friendly 401 responses?
    - Solution: Add specific exception handlers or include them in the generic handler to send meaningful JSON error responses.

6. **Scope and Placement of Exception Catch Blocks in the Filter**
    - How to ensure exceptions like `NegativeArraySizeException` or `DecodingException` are caught in the JWT filter?
    - Key point: The `try-catch` block must wrap the exact code (e.g., token parsing) where these exceptions can occur.

7. **Impact of a Generic `@ExceptionHandler(Exception.class)` in Global Handler**
    - Would having a catch-all handler interfere with specific JWT exception handlers?
    - Clarification: No, specific handlers still take precedence, and the generic handler acts as a fallback for unexpected exceptions.

8. **Including HTTP `status` and `timestamp` Fields in `ApiErrorResponse`**
    - Whether to add `status` (HTTP status code) and `timestamp` (error time) fields in your API error response model?
    - Recommendation: Strongly advised for better debugging, clearer frontend handling, and standardized error responses.

9. **JWT Signature Validation Nuances**
    - Why changing seemingly small characters in the JWT signature part does not always result in validation failure?
    - Explained as an artifact of Base64Url encoding and padding bits that can sometimes cause certain last-character changes to have no effect on the decoded signature.

10. **Testing JWT Exception Scenarios in Postman**
    - How to systematically test various JWT exceptions like expired token, malformed token, invalid signature, unsupported token, missing token, and illegal argument exceptions.
    - Using example tokens and header configurations to trigger each scenario and expect specific JSON error responses with appropriate HTTP status.

11. **Logging Exception Class for Debugging**
    - How to print or log the class of the caught exception using `ex.getClass().getSimpleName()` to identify which exception type is being handled.

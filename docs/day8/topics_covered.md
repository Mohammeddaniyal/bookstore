## **📄 Documentation: Topics Covered (Yesterday → Today)**

### **1. JWT Authentication Flow & Exception Handling**
- Reviewed **JWT parsing and validation** inside a custom `JwtAuthFilter`.
- Discussed why JWT exceptions (`ExpiredJwtException`, `MalformedJwtException` etc.) don’t reach `@RestControllerAdvice` normally due to Spring Security’s internal handling.
- Introduced **`HandlerExceptionResolver` in the filter** to delegate exceptions into Spring MVC’s global exception handling.

***

### **2. Custom & Global Exception Handling**
- Kept `CustomAuthenticationEntryPoint` for **Spring Security's authentication failures** (e.g., missing token).
- Moved JWT parse errors to **Global `@RestControllerAdvice`** for consistent JSON responses.
- Created handlers for:
    - `ExpiredJwtException` → Token expired
    - `MalformedJwtException` → Token format invalid
    - `SignatureException` → Invalid signature
    - `UnsupportedJwtException` → Token type unsupported
    - `IllegalArgumentException` → Token missing or null
    - `NegativeArraySizeException` → Bad Base64 (older versions)
    - `DecodingException` → Invalid Base64 decoding (newer JJWT)
- Added a **generic `@ExceptionHandler(Exception.class)`** as *fallback* with:
    - Special handling for rare/unknown JWT parsing issues.
    - Prevented it from overshadowing specific exception handlers.

***

### **3. ApiErrorResponse Standardization**
- Suggested including:
    - `status` → HTTP status code
    - `timestamp` → When error occurred
    - `errorCode` → Custom code like `TOKEN_EXPIRED`
    - `message` → Human-readable error
    - `errors` → Field-level validation errors (if any)
- Ensures consistent API error responses across all endpoints.

***

### **4. JWT Testing Scenarios via Postman**
Tested:
- **Expired token** → `TOKEN_EXPIRED`
- **Malformed token** → `MALFORMED_TOKEN`
- **Invalid signature** → `INVALID_TOKEN_SIGNATURE`
- **Unsupported token** → `UNSUPPORTED_TOKEN`
- **Missing token** → `AUTH_REQUIRED`
- **Illegal Argument** → `INVALID_TOKEN`
- Also confirmed `DecodingException` handling for bad Base64.

***

### **5. Observations on JWT Behavior**
- Base64Url encoding means changing some last signature characters may **not break token validity** if bits are in padding area → explained why.
- Understanding JWT header.payload.signature format and how signature is verified.

***

### **6. Order & OrderItem Module Planning**
- Entities already created.
- Next planned components:
    - **Repositories:** OrderRepository, OrderItemRepository
    - **Service methods:** `placeOrder`, `getOrderById`, `listOrdersForUser`, `listAllOrders`, `cancelOrder`, `updateOrderStatus`
    - **Controller endpoints** for above operations.
    - **DTOs** for request/response.
    - Access control so customers only view **their** orders.

***

### **7. Security Filtering for User’s Own Orders**
- Using `SecurityContextHolder` to get logged-in username.
- Filtering repository queries by `user.username`.
- Optionally method-level security with `@PreAuthorize`.

***

### **8. Miscellaneous**
- Logging exception class with `ex.getClass().getSimpleName()`.
- Debugging unexpected JWT errors by checking actual thrown exception type.
- Difference between specific vs generic exception handling.
- Decoding issues (`DecodingException`) replacing older `NegativeArraySizeException`.

***

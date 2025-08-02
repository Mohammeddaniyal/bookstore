# Day 3 – Topics Covered, Doubts, and Remaining Tasks

## 🚩 Topics Covered

- **User Registration Improvements**
    - Added password hashing with BCrypt
    - Implemented duplicate username/email checks with custom exceptions
    - Structured and cleaned up API error responses (using Map for error details)
    - Used global exception handler (`@RestControllerAdvice`) for consistent responses

- **Validation and API Response Design**
    - Compared error reporting with `List` vs `Map`
    - Designed `ApiErrorResponse` for both single and multiple errors
    - Standardized message and error codes (e.g., `VALIDATION_ERROR`, `USER_ALREADY_EXISTS`)

- **User Update and Delete Operations** (Discussion)
    - Explored RESTful endpoint design: using `id` vs `username` for updates
    - Recognized need for separate DTOs for update to handle validation properly

- **Authentication and Login Flow**
    - Outlined the login process using email and password
    - Implemented service login method with password verification using `PasswordEncoder.matches`
    - Clarified use of plaintext password vs hashed in login checks
    - Designed `LoginRequestDTO` and response structures
    - Added custom `InvalidCredentialsException` and global handler

- **JWT Integration**
    - Added `jjwt` dependency to the project
    - Discussed and implemented `JwtUtil` utility for token generation and parsing
    - Explored the concepts and benefits of stateless JWT-based authentication
    - Explained that creating a custom JWT utility is the standard approach

- **Spring Security Integration Concepts**
    - Discussed upcoming steps for role-based endpoint protection
    - Reviewed session vs stateless (JWT) security model in REST APIs

- **API Security & Best Practices**
    - Emphasized why custom security is risky vs using proven frameworks like Spring Security
    - Outlined staged plan for integrating Spring Security and JWT into all protected endpoints

---

## ❓ Doubts Addressed

- Do we need to memorize the full `JwtUtil` logic or just understand the overall integration approach?
- Why use `id` over `username` in REST endpoints for updates and deletes?
- Should we require all DTO validation for updates, or use a different DTO than for registration?
- Why can’t we use HTTP Basic Auth for our custom login endpoint?
- Are there risks in rolling our own security logic for authentication?
- Can/will APIs be stateless in this JWT-based approach, and what does that mean?
- Is it normal to feel anxious or confused about abstractions like JWT utilities?
- Do we really need to understand every internal JWT code detail?

---

## ⏳ Remaining Tasks (Short)

- Integrate Spring Security with JWT: implement token filter, set up stateless security config, restrict endpoints by roles.
- Build user **update** and **delete** APIs using a dedicated update DTO and error handling.
- Implement full **Book Management** API (CRUD, search, pagination, admin-only resource protection).
- Complete **Order Management** APIs and database relationships.
- Write unit/integration tests for new features and security flows.
- Add and document Swagger/OpenAPI descriptions and usage examples.
- (Optional) Book reviews, payment integration, admin UI, and other enhancements.


# Coverage Summary – From Yesterday to Today

## 1. Project Setup and Initial User Management
- Initialized Spring Boot project with Gradle.
- Configured database (MySQL preferred).
- Created project folder structure by feature.
- Set up JPA, Lombok, and repositories.
- Designed User Entity and DTOs (`UserRequestDTO`, `UserResponseDTO`).
- Implemented User Registration API with validation and password hashing (BCrypt).
- Added duplicate username/email handling with custom exceptions.
- Setup global exception handling with standardized API error responses.
- Implemented User login endpoint skeleton (DTOs and controller).

## 2. Authentication and JWT Integration
- Discussed pros and cons of JWT vs session-based authentication.
- Designed login API flow: receive email/password, authenticate user, issue JWT token.
- Created `LoginRequestDTO` and `LoginResponseDTO`.
- Completed login service method:
    - Verify user by email,
    - Use `PasswordEncoder.matches()` to validate password,
    - Generate JWT token upon success.
- Added jjwt library dependency and created custom `JwtUtil` class:
    - Generate tokens,
    - Extract username and roles,
    - Validate token signature and expiration.
- Clarified that you pass plaintext password to `passwordEncoder.matches()` (no manual hashing).
- Explained statelessness: no session storage, every request must have JWT.

## 3. Spring Security Integration and JWT Request Filtering
- Created `JwtAuthFilter` extending `OncePerRequestFilter`:
    - Extract token from `Authorization` header,
    - Validate token and set authentication in security context.
- Explained the key conditional check in the filter (`username != null && SecurityContextHolder.getContext().getAuthentication() == null`).
- Implemented `CustomUserDetailsService` in `security` package:
    - Load user by email (not username),
    - Map roles to `GrantedAuthority`.
- Discussed package organization best practices (keeping security-related code separate from service layer).
- Configured Spring Security with:
    - Stateless session management,
    - Disabled CSRF using lambda DSL (`csrf(csrf -> csrf.disable())`),
    - Publicly accessible register and login endpoints,
    - Role-based access on `/api/books/**` and `/api/orders/**`,
    - Added JWT filter before `UsernamePasswordAuthenticationFilter`.
- Updated configuration norms for latest Spring Security 6+ versions.
- Addressed deprecation of `.csrf().disable()` and `DaoAuthenticationProvider` usage with modern alternatives.

## 4. Testing and Debugging Authentication
- Verified full flow:
    - Register user,
    - Login returns JWT token,
    - Protected endpoints secured by JWT and role restrictions.
- Resolved mismatch between JWT subject and user lookup key (use email consistently).
- Explained expected HTTP responses: `403 Forbidden` when unauthorized or forbidden.
- Clarified JWT token usage and repeated authentication on every stateless request.
- Discussed why email is preferred for login instead of username.

## 5. Soft Skills and Learning Guidance
- Discussed approach to understanding JwtUtil—focus on the big picture and usage rather than memorizing all details.
- Encouraged using JwtUtil as a reusable utility with focus on when/how to generate and validate tokens.
- Created Day 3 MD summary (topics covered, doubts addressed, remaining tasks).

---

This summary encapsulates your project's journey from setup through full JWT-based authentication and Spring Security integration.

If you want, I can prepare a detailed actionable task list or help create a consolidated documentation file for your reference — just ask!

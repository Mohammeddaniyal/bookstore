Here’s a **deep, step-by-step explanation** of what happens—at the level of specific code and framework components—when a client makes an **unauthorized request** to a protected API endpoint in your Spring Boot + JWT + Spring Security app. This means: requesting an endpoint that requires authentication without providing a JWT, or providing an invalid/expired JWT.

# Unauthorized Request Scenario (No or Invalid JWT)

## Example 1: Client Requests a Protected API Without JWT

### Client Request

```
GET /api/books
(no Authorization header)
```

## Step 1: **DispatcherServlet Receives the Request**

- Spring’s DispatcherServlet matches the route and dispatches to the filter chain, targeting a protected endpoint (e.g., `/api/books`).

## Step 2: **Spring Security Filter Chain Executes**

- Your SecurityConfig declares (for `/api/books/**`):

  ```java
  .authorizeHttpRequests(auth -> auth
      .requestMatchers("/api/books/**").hasRole("ADMIN")
      .anyRequest().authenticated()
  )
  .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
  ```

- So, all `/api/books` requests demand a user with the `ADMIN` role.

## Step 3: **JwtAuthFilter Executes**

- Inside your `JwtAuthFilter.doFilterInternal()`:

  ```java
  final String authHeader = request.getHeader("Authorization");
  String username = null;
  String jwtToken = null;

  if (authHeader != null && authHeader.startsWith("Bearer ")) {
      jwtToken = authHeader.substring(7);
      username = jwtUtil.extractUsername(jwtToken);
  }
  ```

- Since there is **no `Authorization` header**, `authHeader` is `null` and the code block to extract and validate JWT **is completely skipped.**
- This means:
    - `username` is `null`,
    - No user details are loaded,
    - No validation occurs,
    - **No authentication is set in the security context.**

  ```java
  if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      // This block is NOT executed since username is null
  }
  ```

- The filter proceeds to continue down the filter chain:

  ```java
  filterChain.doFilter(request, response);
  ```

## Step 4: **Spring Security Authorization Decision**

- Now, Spring Security checks if the request is authenticated (i.e., expects an `Authentication` object in `SecurityContextHolder`).

- Because **no JWT was provided and no authentication was set up:**

    - The authentication object in the security context is `null`.
    - The `.hasRole("ADMIN")` requirement is NOT satisfied since there is no authenticated user at all.

## Step 5: **Access Is Denied – Exception Handling**

- Spring Security determines:
    - The request is unauthenticated,
    - The endpoint requires authentication and a specific role.

- The **ExceptionTranslationFilter** in Spring Security’s chain recognizes this and:
    - Responds with **401 Unauthorized** (if no authentication present),
    - Or responds with **403 Forbidden** (if some authentication exists but user lacks the required authority).

- In this case:
    - For NO JWT, expect **HTTP 401 Unauthorized**
    - For INVALID JWT (fails verification in filter), no `Authentication` is set → still **401 Unauthorized**
    - For VALID JWT but wrong role → **403 Forbidden**

- Default response body (can be customized via global exception handler) will indicate that authentication is required or access is denied.

## Example 2: Client Requests with an Invalid or Expired JWT

### Client Request

```
GET /api/books
Authorization: Bearer bad.or.expired.jwt
```

### Code flow in `JwtAuthFilter`:

- `authHeader` is present and starts with `"Bearer "`.
- Attempts to extract `username` via `jwtUtil.extractUsername(jwtToken)`:
    - If JWT is expired or malformed, this throws or returns `null`.
    - So, `username` is left as `null`.

- The guard:

  ```java
  if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      // NOT executed, username is null
  }
  ```

- No setting of authentication; request remains unauthenticated.

- The filter chain continues, and Security moves to access check as above.
- Again: **401 Unauthorized** is returned to client.
- If your filter logs errors, you might see logs indicating token parsing or validation failure.

# **Summary Table**

| Scenario                            | Line(s) executed                          | Effect                                                          | Client gets    |
|--------------------------------------|-------------------------------------------|------------------------------------------------------------------|---------------|
| No Authorization header              | JWT extraction/validation skipped         | No authentication in context                                     | 401 Unauthorized |
| Invalid/expired JWT                  | JWT extraction fails, guard not entered   | No authentication in context                                     | 401 Unauthorized |
| JWT valid, but lacks required role   | filter sets authentication, but role fails| Proceeds, but .hasRole() check fails                             | 403 Forbidden     |

# Key Takeaways for Developers

- **JwtAuthFilter only sets authentication for valid JWTs.** No token → auth context is empty.
- **Spring Security’s context is per-request and is empty by default.** Only the presence of a valid JWT creates an authenticated context.
- **Access control is enforced AFTER the filter:**
    - If no authentication in context, protected endpoints immediately return 401 Unauthorized.
    - If authentication is present, but roles do not match, returns 403 Forbidden.

- **Global exception handling and response format:** These status codes and error bodies can be customized in your app with an exception handler or customized access denied handler.

If you’d like, I can also show the same mechanics for custom error response handling, or for any other special case in your filter or security chain!
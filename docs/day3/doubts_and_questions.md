# User Questions – Session Summary (Yesterday to Today)

## Authentication and Security General Questions
- Is there any bad thing using our own security for login?
- What's your call? How are we going to handle login with JWT and sessions?
- Do we need to hash the password before comparing in passwordEncoder.matches()?
- Can we use jjwt for JWT implementation?
- JwtUtil not found error after adding dependency — what to do?
- How are we going to handle token parsing?
- Is creating a custom JwtUtil the right approach?
- Do we need to memorize JwtUtil code or just understand the approach?
- Can't we use jjwt library? (short answer)
- Should APIs in this project be stateless?
- What exactly does stateless mean?
- If csrf.disable() is deprecated, what is the correct way to disable CSRF in Spring Security 6+?
- Is DaoAuthenticationProvider deprecated? Should we still use it?
- Why isn’t DaoAuthenticationProvider replaced in your example for Spring Security 6+?

## Implementation Specific Questions
- I have done JwtUtil part, what's next?
- Done up to creating login endpoint and DTO, what's next?
- Can you provide the login service implementation to authenticate and generate JWT?
- Do we hash incoming login password before calling passwordEncoder.matches()?
- Can you provide the JWT dependency for Gradle/Maven?
- JwtUtil class explanation and code example request.
- Should CustomUserDetailsService be in the security package or service package?
- Should UserDetailsService implementation be kept in security or service package?
- I want explanation of the CustomUserDetailsService class.
- Explain the condition: `if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)`
- Explain with different cases why that condition is there.
- If every request is stateless, why would that condition ever be false, e.g. after multiple requests or server restart?
- Is that condition only a best practice, mostly redundant?
- Explain the token validation and setting authentication lines in JwtAuthFilter at the end of the session.
- Why is `loadUserByUsername` using email, but JwtAuthFilter passes username for lookup? Will it fail?
- Why not use username for login instead of email?
- I am sticking with email for login — how to ensure consistency?
- I got 403 Forbidden when accessing protected endpoints — is this correct?
- Is this SecurityConfig code correct with Autowired UserDetailsService and JWT filter?
- Why aren’t you updating DaoAuthenticationProvider usage for Spring Security 6+?
- Can you rewrite the full SecurityConfig using the new Spring Security version?
- Can you explain why `csrf.disable()` is deprecated and what to replace it with?
- Can you explain in short whether my logic in JwtAuthFilter to check authentication before setting context is necessary?
- Is my security setup with `anyRequest().authenticated()` giving 403 Forbidden correct behavior?
- Is my JwtAuthFilter and CustomUserDetailsService code consistent regarding email vs username?

## Project Management and Next Steps
- What have we covered so far and what should we ideally handle next?
- What next task should I do after creating login API endpoint?
- What’s the next major task after JWT token generation?
- Create the JWT filter step by step.
- Implement CustomUserDetailsService step by step.
- Create SecurityConfig for Spring Boot 3 / Spring Security 6 with JWT.
- Should the login service be in security or service package?
- Should I memorize JwtUtil code or just understand the general approach?
- Can you create a Day 3 summary markdown file with what we covered, doubts, and remaining tasks?
- Explain the whole JwtUtil utility from basics to deep level with simple language.
- Explain why JWT subject and user lookup field must match, and how to fix mismatch (email vs username).
- Why do people use email instead of username for login?
- Next steps after completing JWT utility and SecurityConfig.
- What does 403 Forbidden mean when accessing a JWT protected endpoint?

---

*If you want, I can also provide a categorized TODO list or detailed answers recap based on these questions.*


# Day 2 Topics Covered

- **Creating the Service Layer for Users**  
  Introduced `UserService` interface and `UserServiceImpl` to encapsulate business logic.

- **Why Use Interfaces in Service Layer?**  
  Explained benefits of interfaces for testability, flexibility, and clean architecture.

- **Spring Data JPA Repository Auto-Implemented Methods**  
  How method names like `findByUsername` get implemented automatically.

- **Additional Service Methods: Add as Needed vs. Upfront**  
  Discussed strategy for CRUD methods in service layer.

- **Using DTOs (Data Transfer Objects)**  
  Created `UserRequestDTO` and `UserResponseDTO` for API input/output.

- **Why Separate DTOs Are Better Than a Single DTO**  
  Explained flexibility and security reasons for distinct request/response DTOs.

- **Controller Implementation Using DTOs**  
  Mapped between DTOs and entities in `UserController`, handled API requests/responses.

- **Understanding Lombok’s @Builder Annotation**  
  Explained builder pattern and usage with Lombok.

- **Handling Role Field in User DTOs**  
  Decided to exclude `roles` from `UserRequestDTO` and assign roles server-side.

- **Validation in DTOs Using @Valid and Bean Validation**  
  Added validation annotations, ensured request data correctness.

- **Global Exception Handling for Validation Errors**  
  Created `@RestControllerAdvice` to intercept validation exceptions and send meaningful responses.

- **How @Valid Works in Spring Boot Controllers**  
  Clarified behavior when validation passes or fails on incoming requests.

- **Disabling Security Temporarily**  
  Discussed methods to disable or bypass Spring Security during development.

# Questions & Doubts Discussed

- Why create a service interface instead of using just a service class?
- How does Spring Data JPA provide implementations for finder methods like `findByUsername`?
- Should we add all CRUD service methods upfront or add them on demand?
- Should `roles` be included in DTOs, and how to handle roles securely?
- Is it better to use a single DTO or separate request and response DTOs?
- What exactly does Lombok’s `@Builder` do and how to use it?
- How to correctly map between DTOs and entities in the controller?
- How does `@Valid` annotation work and what happens on validation failure?
- How to disable Spring Security temporarily during development?

# Summary of What We Did

- Designed and implemented the **User service layer** with interface and implementation.
- Created **request and response DTOs** for user data, including validation.
- Built a **REST controller** that accepts requests with `UserRequestDTO` and responds with `UserResponseDTO`.
- Added **validation logic** and implemented **global exception handling** for clean API error responses.
- Discussed best practices for roles management, service design, and clean API architecture.
- Tested controller endpoints and ensured the flow works end-to-end.

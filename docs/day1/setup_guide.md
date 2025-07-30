# Day 1 Setup Guide – Spring Boot Bookstore Project

## 1. Project Initialization

- Go to [Spring Initializr](https://start.spring.io/) or use your IDE’s generator.
- Choose:
    - **Project:** Gradle (Groovy)
    - **Language:** Java
    - **Group:** `com.daniyal.bookstore`
    - **Artifact/Name:** `bookstore-api`
    - **Java Version:** 17 or higher

- **Add dependencies:**
    - Spring Web
    - Spring Data JPA
    - MySQL Driver
    - Lombok
    - Spring Security

- Download and unzip the project, then open it in IntelliJ IDEA.

## 2. MySQL Database Setup

- Open MySQL Shell and switch to SQL mode:
  ```
  \sql
  ```
- Connect as root:
  ```
  \connect root@localhost
  ```
- Create the database:
  ```sql
  CREATE DATABASE bookstore;
  ```
- Create a dedicated app user and grant privileges:
  ```sql
  CREATE USER 'bookuser'@'localhost' IDENTIFIED BY 'yourpassword';
  GRANT ALL PRIVILEGES ON bookstore.* TO 'bookuser'@'localhost';
  FLUSH PRIVILEGES;
  ```

## 3. Spring Boot Application Configuration

- Open `src/main/resources/application.yml` (or `application.properties`).
- Add the following (edit username/password as needed):

  ```yaml
  server:
    port: 8080

  spring:
    datasource:
      url: jdbc:mysql://localhost:3306/bookstore
      username: bookuser
      password: yourpassword
    jpa:
      hibernate:
        ddl-auto: update
      show-sql: true
      properties:
        hibernate:
          dialect: org.hibernate.dialect.MySQL8Dialect
  ```

## 4. IDE & Lombok Setup

- Install the Lombok plugin in IntelliJ.
- Go to **Settings > Build, Execution, Deployment > Compiler > Annotation Processors** and enable annotation processing.

## 5. Create Initial Project Structure

- Inside `src/main/java/com/daniyal/bookstore`, add these packages:
    - `model` – for entities (e.g., `User.java`)
    - `repository` – for repository interfaces
    - (later: `service`, `controller`, `dto`, etc.)

## 6. Implement the User Entity

- In the `model` package, create the `User` class with basic fields:
    - `id`, `username`, `email`, `password`, `roles` (as `List`)
- Use JPA and Lombok annotations for concise, auto-mapped code.

## 7. Implement UserRepository

- In the `repository` package, create `UserRepository.java` and extend `JpaRepository`.
- Define methods like `findByUsername(String username)` and `findByEmail(String email)`.

## 8. Test Database Integration

- Create a JUnit test (e.g., `UserRepositoryTest`).
- Use `@DataJpaTest` with `@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)` to run tests against your local MySQL.
- Save and retrieve a user entity to confirm end-to-end DB operation works.

## 9. Troubleshooting

- Common issues: DB connection failures, wrong user credentials, YAML syntax errors, conflicting test annotations.
- Consult `doubts_and_answers.md` for solutions during setup.

**End of Day 1 Setup Guide**

This guide walks you from project generation to first working DB test. Use it as your checklist for repeating the setup, onboarding, or writing about your journey! Let me know if you’d like a template for the next day’s guide or more detailed code snippets for any step.
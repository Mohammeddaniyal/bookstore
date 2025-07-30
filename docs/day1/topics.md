# Topics Covered in Spring Boot Bookstore Project

## Project Setup & Tooling
- Spring Boot project initialization with Gradle
- Lombok setup and configuration
- MySQL database setup and user management (via MySQL Shell)
- Configuring `application.properties` and `application.yml`

## Core Spring Boot & JPA Concepts
- JPA Entity modeling (`@Entity`, `@Table`, `@Id`, `@Column`)
- Lombok annotations (`@Getter`, `@Setter`, `@Builder`, etc.)
- Using `@ElementCollection` for collections in entities
- Database relationships overview (One-to-Many, Many-to-Many via join entity)

## Spring Data JPA & Repository
- Creating Repository interface extending `JpaRepository`
- Custom finder methods (`findByUsername`, `findByEmail`)

## Testing
- Writing unit/integration tests with JUnit 5 and Spring Boot Test
- Using `@DataJpaTest` for JPA slice tests
- Using `@AutoConfigureTestDatabase` to manage test database configuration
- Transaction rollback behavior in tests

## Common Pitfalls and Error Handling
- Conflicts between `@DataJpaTest` and `@SpringBootTest`
- Database connection issues in tests
- YAML syntax and configuration mistakes

## Hibernate & Spring Boot Autoconfiguration
- Automatic table creation/update (`spring.jpa.hibernate.ddl-auto`)
- Connection pooling with HikariCP
- Understanding the Conditions Evaluation Report

## MySQL Specifics
- Creating database and users
- Granting privileges and user management
- Running MySQL Shell commands

---

*This file will be updated regularly with new topics as the project progresses.*

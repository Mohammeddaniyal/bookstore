# Building a Spring Boot Bookstore: Day 0 – Getting Oriented

Welcome to **“Building a Spring Boot Bookstore”**, a beginner-friendly series where you’ll learn by doing. Day 0 is all about laying a rock-solid foundation: understanding core concepts, familiarizing yourself with project structure, and setting up your environment. By the end of today, terms like *auto-configuration*, *entities*, and *repositories* will be clear, so Day 1’s coding adventure feels natural.

## Table of Contents

1. [Why This Series?](#why-this-series)
2. [Key Concepts & Terminology](#key-concepts--terminology)
3. [Project Structure Walkthrough](#project-structure-walkthrough)
4. [Prerequisites & Environment Setup](#prerequisites--environment-setup)
5. [How to Follow Along](#how-to-follow-along)
6. [Day 1 Preview](#day-1-preview)

## Why This Series?

Most Spring Boot tutorials assume you already know spring’s layers, JPA magic, or Gradle. Here, **every new term** is introduced gently, then reinforced with code. Even if you’ve only dabbled in Java and know _some_ Spring Boot basics, you’ll be able to follow along, build real features, and understand *why* each piece matters.

## Key Concepts & Terminology

### Spring Boot & Auto-Configuration
- **Spring Boot** is a framework that pre-configures your app so you write minimal setup code.
- **Starter Dependencies** group common libraries (e.g., `spring-boot-starter-web`).
- **Auto-Configuration** means Spring “guesses” sensible defaults—like an embedded Tomcat server for REST APIs—so you don’t configure everything by hand.

### Dependency Management
- **Gradle** (or Maven) manages libraries your project needs (like JPA or Lombok).
- Your `build.gradle` declares dependencies; Gradle downloads them and compiles your code.

### JPA, Hibernate & ORM
- **JPA (Java Persistence API)** is a standard for mapping Java objects to database tables.
- **Hibernate** is the most popular JPA *implementation*—it runs the SQL behind the scenes.
- You define **entities** (annotated with `@Entity`) and Hibernate creates tables for you.

### Layers of a Spring Boot App
1. **Model (Entity):** Java classes representing database tables.
2. **Repository:** Interfaces extending `JpaRepository` for CRUD operations—no SQL required.
3. **Service:** Business logic (e.g., user signup, book ordering).
4. **Controller:** REST endpoints handling HTTP requests and responses.

### Configuration & Beans
- **`@SpringBootApplication`** is a meta-annotation enabling component scanning and auto-configuration.
- **Beans** are objects managed by Spring’s IoC (Inversion of Control) container.
- **Dependency Injection** (“DI”) injects beans where needed (e.g., a `UserService` into a `UserController`).

### Testing Annotations
- **`@DataJpaTest`** loads only your JPA layer for fast repository tests.
- **`@SpringBootTest`** loads the full application context for integration tests.

### Database Basics
- **Schema & Tables:** A schema is a collection of tables; each table holds rows of data.
- **JDBC URL:** Connection string (e.g., `jdbc:mysql://localhost:3306/bookstore`).
- **Connection Pool:** Library (HikariCP) managing database connections for speed and efficiency.

## Project Structure Walkthrough

```
bookstore-api/
├── build.gradle                 # Gradle build script
├── settings.gradle              # Project settings
├── src/
│   ├── main/
│   │   ├── java/com/daniyal/bookstore/
│   │   │   ├── model/            # JPA entities (e.g., User.java)
│   │   │   ├── repository/       # Spring Data interfaces
│   │   │   ├── service/          # Business logic classes
│   │   │   └── controller/       # REST controllers
│   │   └── resources/
│   │       ├── application.yml   # Config (DB URL, JPA settings)
│   │       └── static/           # Static assets (later)
│   └── test/
│       └── java/com/daniyal/...  # JUnit tests
└── docs/
    └── day0/
        ├── topics.md            # Day 0 table of contents
        ├── setup_guide.md       # Environment setup steps
        └── concepts/            # Detailed concept files
```

## Prerequisites & Environment Setup

1. **Java JDK 17+** installed and `JAVA_HOME` configured.
2. **IntelliJ IDEA** (Community or Ultimate) with **Lombok plugin** and _annotation processing_ enabled.
3. **MySQL** (or install H2 for in-memory tests) and basic credentials.
4. **Git** for version control—clone the repo and create a `day0` branch.

## How to Follow Along

- **Branching:** Use a new Git branch for each day (`day0`, `day1`, etc.).
- **Code Snippets vs. Full Source:** Inline snippets in the blog; full code on GitHub.
- **Ask Questions:** Comment on the blog or open an issue on the repo if stuck.

## Day 1 Preview

Tomorrow we’ll:

1. Generate the Spring Boot project with Gradle.
2. Create a MySQL database and dedicated user.
3. Implement the `User` entity and `UserRepository`.
4. Configure `application.yml`.
5. Write our first `@DataJpaTest` verifying real database integration.

By the end of Day 1, you’ll have code that actually connects to MySQL, auto-creates tables, and passes a real test. No more black-box tutorials—this is hands-on from the start!

**Ready to build?** Install your tools, clone the repo, create branch `day0`, and review today’s concepts. Tomorrow, Day 1 kicks off with actual code—see you then!
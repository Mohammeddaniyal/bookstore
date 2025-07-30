# 2. Gradle Build Tool

## What Is It?
Gradle is a modern build automation tool used to manage dependencies and build lifecycle (compile, test, run) in Java projects.

## Why Use It?
- Handles all project dependencies with simple scripts
- Automates compile, test, and package tasks

## How Does It Work?
Gradle reads the `build.gradle` file, downloads all required libraries, and orchestrates builds and tests.

## How Is It Used Here?
Your Bookstore project uses Gradle to pull in Spring Boot, JPA, MySQL, Lombok, and other dependencies.

## Example
dependencies {
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'mysql:mysql-connector-java'
implementation 'org.projectlombok:lombok'
}

## Common Pitfalls
- Not triggering a Gradle sync in your IDE after changing dependencies
- Using an incorrect Gradle wrapper or local version

## Troubleshooting Tips
- Refresh dependencies after editing `build.gradle`
- Use Gradle’s `--refresh-dependencies` flag for stubborn issues

## Related Concepts
- Maven, Dependency Management

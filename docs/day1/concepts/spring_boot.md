# 1. Spring Boot Project Initialization

## What Is It?
Spring Boot is a framework for fast, opinionated Java backend development. It jumpstarts projects with embedded servers, auto-configuration, and minimal setup.

## Why Use It?
- Rapid project setup—no need for heavy manual config
- Embedded server for REST APIs out of the box
- Huge ecosystem and strong community

## How Does It Work?
You generate a base project (using Spring Initializr or your IDE), choose your dependencies, and Spring Boot autoconfigures the environment.

## How Is It Used Here?
You used Spring Initializr and Gradle to create a skeleton project for the Bookstore REST API.

## Example
- Downloading project from Spring Initializr with dependencies (Web, JPA, MySQL, Lombok).
- Project has a `@SpringBootApplication` main class.

## Common Pitfalls
- Forgetting to add essential dependencies at generation can require manual edits later.
- Not matching Java version with Spring Boot compatibility.

## Troubleshooting Tips
- Use the Spring Initializr for consistent results.
- If the app doesn’t run after project creation, check for missing or conflicting dependencies.

## Related Concepts
- Gradle or Maven, Embedded Tomcat

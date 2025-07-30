# Day 1 Challenges & Learnings – Spring Boot Bookstore Project

---

## 1. Setting Up the Database and User Access

**Challenge:**
- Unsure whether to use the MySQL root user or create a dedicated database user for the project.
- Had confusion about proper SQL commands in MySQL Shell.

**How I Overcame It:**
- Learned best practice: always create a dedicated user for each app, grant only necessary privileges.
- Practiced SQL commands in MySQL Shell: `CREATE DATABASE`, `CREATE USER`, `GRANT PRIVILEGES`.
- Documented user creation and privilege-granting steps.

**Lesson Learned:**  
Setting up principle of least privilege from the beginning keeps the app secure and makes configuration easier to debug.

---

## 2. Configuring application.yml and Properties

**Challenge:**
- Typos in property keys (`appplication` vs. `application`; `hibrenate` vs. `hibernate`).
- Trouble with YAML indentation.

**How I Overcame It:**
- Compared with official Spring Boot documentation and asked clarifying questions.
- Used code formatting and an online YAML validator to check structure.

**Lesson Learned:**  
Even a small typo or misaligned space can break Spring configuration. Always check spelling and use validators!

---

## 3. JPA Entity Design and Annotations

**Challenge:**
- Should “roles” be stored as a single string or a collection?
- Unsure which JPA/Lombok annotations to use, and whether to mark fields not-null/unique.

**How I Overcame It:**
- Learned about `@ElementCollection` and why it’s needed for lists in relational DBs.
- Added constraints like `@Column(unique = true, nullable = false)` for `username` and `email`.

**Lesson Learned:**  
Relational DBs require extra design compared to document stores. Readability, validation, and DB integrity all start with proper annotation choices.

---

## 4. JPA Testing: DataJpaTest and Database Confusion

**Challenge:**
- Combined `@DataJpaTest` and `@SpringBootTest` in a test, causing a configuration exception.
- Test failed: “Failed to replace DataSource with an embedded database for tests.”

**How I Overcame It:**
- Learned that you must never combine both annotations; each has a different bootstrapping purpose.
- Added `@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)` to use my real MySQL DB for tests.
- Recognized that test data is rolled back after each run—expected behavior for data consistency.

**Lesson Learned:**
- Use only one major test annotation per test class.
- Embedded DBs are optional in Spring Boot; if you use your real DB, update your test config.
- Read the full error log and Spring’s Conditions Evaluation Report for clues!

---

## 5. Lombok Plugin

**Challenge:**
- Missed enabling annotation processing in IntelliJ at first, causing Lombok-generated getters/setters to be “missing” at compile time.

**How I Overcame It:**
- Installed the Lombok plugin and enabled annotation processing in IDE settings.

**Lesson Learned:**  
Lombok can only work if annotation processing is enabled; IDE integration is essential for smooth experience.

---

## 6. General Takeaways

- “Google your errors”—often the top Stack Overflow or Spring docs page is extremely helpful.
- Write down every error, what caused it, and the fix; this makes future debugging much faster.
- Code (and config) copy-paste errors are the biggest source of silly bugs—double check every snippet.

---

### What I’d Tell Someone Starting Out

1. Start slow, verify every step.
2. Always check for typos and indentation before assuming some advanced issue!
3. Read both the top and bottom of error logs—Spring Boot tells you explicitly what’s wrong.
4. Modularize: work one file/functionality at a time, write down what you tried and why.

---

*End of Day 1 challenge log. This will help me write a more honest blog post, reinforcing how real learning is messy but always valuable!*

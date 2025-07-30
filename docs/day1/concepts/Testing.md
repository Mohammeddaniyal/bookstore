# Testing with @DataJpaTest and Test Database Configuration

## What Is @DataJpaTest?

`@DataJpaTest` is a specialized Spring Boot test annotation designed for quickly and efficiently testing JPA repositories and entities in isolation. It loads only the **JPA components** of your application, such as repositories, entities, and their configuration—**not the full Spring context or web/server layers**.

---

## Why Use It?

- **Fast and focused:** Loads only what's needed for JPA tests, making tests quicker.
- **Automatic DB cleanup:** Each test runs in a transaction that's rolled back afterward, keeping the database clean.
- **Ensures correctness:** Verifies mapping and repository methods before your business logic and API layers use them.

---

## How Does It Work?

- By default, `@DataJpaTest` tries to configure an **embedded, in-memory database** (like H2 or HSQL) for testing, so your real local/production database is never affected.
- Spring Boot scans your project's repository interfaces and entity classes, setting up a lightweight test context for just data-access code.
- After each test, any data changes are rolled back for isolation and repeatability.

---

## How Is It Used Here?

**We created a JUnit test for the `UserRepository` in the `src/test/java/com/daniyal/bookstore/repository` directory:**
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Use real MySQL, not embedded
public class UserRepositoryTest {

@Autowired
private UserRepository userRepository;

@Test
public void testSaveAndFindByUsername() {
User user = User.builder()
.username("junituser")
.email("junituser@example.com")
.password("testpassword")
.build();

    userRepository.save(user);

    Optional<User> found = userRepository.findByUsername("junituser");
    assertThat(found).isPresent();
    assertThat(found.get().getEmail()).isEqualTo("junituser@example.com");
}
}


- **`@DataJpaTest`:** Configures a test context for repositories/entities only.
- **`@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)`:** Instructs Spring Boot to use your real MySQL database instead of trying to swap in an embedded database (since H2 or HSQL wasn't on the classpath).
- The test saves a user, retrieves it, and asserts its presence—all within a rolled-back transaction.

---

## What Problem Occurred When @SpringBootTest Was Used With @DataJpaTest?

- **Issue:** Applying both `@SpringBootTest` and `@DataJpaTest` on the same test class caused a Spring Boot context startup error:

  > Configuration error: found multiple declarations of @BootstrapWith...

- **Why:** Both annotations try to configure the application context in different, incompatible ways (`@DataJpaTest` for slice tests, `@SpringBootTest` for loading the entire application).
- **Solution:**
    - **Use only one:** For JPA repository/data layer tests, keep only `@DataJpaTest` and remove `@SpringBootTest`.
    - For end-to-end or integration tests (where you want to load everything), use only `@SpringBootTest`.
- **Lesson:** Never combine `@SpringBootTest` and `@DataJpaTest` on the same test class.

---

## Additional Notes on Configuring the Test Database

- If you do not use `@AutoConfigureTestDatabase(replace = NONE)`, Spring Boot will look for an embedded/in-memory database.
- To use your real local MySQL (as you did), this extra annotation is essential.
- Alternatively, to use an in-memory DB for speed and isolation, add the H2 dependency and stick to the default setting.

---

## Common Pitfalls

- Combining multiple context-setup annotations on a test class (`@SpringBootTest` + `@DataJpaTest`)—pick one only!
- Not specifying `replace = NONE`: leads to “Failed to replace DataSource with an embedded database for tests” if no embedded DB in your dependencies.
- Be sure your real database server is running and credentials are correct when using your actual DB.

---

## Summary

- Use `@DataJpaTest` to create fast, focused, and auto-cleaned JPA tests.
- Use `@AutoConfigureTestDatabase(replace = NONE)` if you want to test against your real configured DB.
- Never combine `@SpringBootTest` and `@DataJpaTest` in the same test class.
- Each test is rolled back automatically, so your database stays clean.


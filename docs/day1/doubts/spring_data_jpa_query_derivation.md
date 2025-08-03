### ❓ Question:
How does Spring Data JPA automatically implement methods like `findByUsername` in a repository interface?

---

When your repository interface (e.g., `UserRepository`) extends `JpaRepository`, Spring Data JPA **automatically provides implementations** for a variety of methods based on naming conventions you declare, such as `findByUsername`, `findByEmail`, etc.

**How it works in short:**
- Spring Data analyzes your method names using a **query derivation** mechanism.
- It infers the database query from the method name, matching the field names of your entity (`User`).
- **You only define method signatures**, no implementation needed.

**Limitation:**
- You can only create such methods based on the **fields declared in the `User` entity** (or properties accessible via getters).
- If your entity has 4 fields, you can create finder methods only involving those fields (e.g., `findByUsernameAndEmail`), but not arbitrary database columns that don’t exist on that entity.

In future, if you need more complex queries or operations beyond fields, you can:
- Use **@Query annotation** to write custom JPQL/SQL queries, or
- Create a **custom repository implementation** with your own method logic.

So yes — by default, Spring Data JPA methods are limited to your entity’s fields, and method names must match those fields for auto-implementation.

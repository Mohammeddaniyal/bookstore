# JPA Relationship Deep Dive: @OneToMany and @ManyToOne (Owning vs Non-Owning)

This document explains in detail how `@OneToMany` and `@ManyToOne` relationships work internally in JPA/Hibernate, covering:
- What the owning and non-owning sides mean
- How foreign keys are managed
- How fetching behaves for each side
- The impact of `mappedBy` on schema and persistence
- How cascading and lazy loading fit into the picture
- Practical code examples and SQL query behavior

Use this as a reference for understanding how JPA maps entity relationships to your database and how those mappings affect performance, schema generation, and data consistency.
***
The internal behavior of JPA when handling `@OneToMany` and `@ManyToOne` relationships, especially with the `mappedBy` attribute indicating ownership, is fundamental to understanding entity fetching, foreign key management, and loading strategies.

Here's a detailed explanation of how these annotations work internally during fetching, with an example of owning and non-owning sides:

***

## Understanding `@OneToMany` and `@ManyToOne` Internally in JPA

### 1. Relationship Ownership: Owning vs. Non-Owning Side

- **Owning side**: The entity that contains the **foreign key column** in the database table. Only the owning side controls the relationship in JPA (updates, inserts foreign keys).
- **Non-owning (inverse) side**: The other side which refers to the owning side using the `mappedBy` attribute. It doesn't have its own foreign key column and does not control the relationship.

***

### 2. How `@ManyToOne` Works Internally

- The `@ManyToOne` side is always the **owning side** by default.
- It has a foreign key column in its table referencing the primary key of the parent entity.
- When fetching, JPA loads the child entity and can use the foreign key to join or fetch the associated parent entity.

### 3. How `@OneToMany(mappedBy = "...")` Works Internally

- The `@OneToMany` side is the **inverse/non-owning side**.
- It specifies `mappedBy` to indicate the field in the owning side entity that owns the relationship.
- This side does not have a foreign key column and database-wise, it does not cause any schema change directly.
- When fetching from this side (e.g., fetch a parent and want its child list), JPA runs an extra query or join based on the foreign key column on the owning side.

***

## Example Scenario: User and Order Relationship

Imagine:

- A `User` can have many `Order`s.
- Each `Order` belongs to exactly one `User`.

### Entity Code Snippet

```java
@Entity
public class User {
    @Id
    private Long id;
    
    @OneToMany(mappedBy = "user")    // Non-owning side, "user" in Order owns relationship
    private List orders;
}

@Entity
public class Order {
    @Id
    private Long id;
    
    @ManyToOne                     // Owning side (has FK column "user_id")
    @JoinColumn(name = "user_id")
    private User user;
}
```

***

## Internal Behavior When Fetching

### Fetching an `Order` (Owning side)

- JPA loads the `Order` row; it has a `user_id` column storing the FK.
- If `user` is `EAGER` or accessed during the session (and fetch type is Lazy but session open), JPA performs a join query or separate select to load the referenced `User` entity.
- Since `Order` owns the relationship via `@ManyToOne`, the foreign key column `user_id` is present here.

### Fetching a `User` and accessing `orders` (Non-owning side)

- JPA loads the `User` entity (no foreign keys here).
- The `orders` list is marked `@OneToMany(mappedBy = "user")`, so JPA knows the foreign key `user_id` is on the `Order` table.
- When accessing `user.getOrders()`, if lazy, JPA executes a separate query:

  ```sql
  SELECT * FROM orders WHERE user_id = ?
  ```

- This fetches the list of orders associated with the user by querying the owning side’s foreign key column.
- JPA does **not** manage relationship changes from this non-owning side; only the owning side writes the foreign key.

***

## Why This Design?

- Database foreign keys are stored on the “many” side in `@ManyToOne`.
- The “one” side can view the relationship but can't directly write the foreign key because it isn’t stored there.
- This prevents ambiguity and ensures consistent updates.

***

## Summary Table

| Annotation        | Side Type       | Owns Foreign Key? | Has FK Column?   | Relationship Managed By               | Fetch Behavior                                      |
|-------------------|-----------------|-------------------|------------------|-------------------------------------|----------------------------------------------------|
| `@ManyToOne`      | Owning          | Yes               | Yes (`user_id`)  | Yes (foreign key updates)            | Loads FK directly; can join or lazy load as needed |
| `@OneToMany(mappedBy)` | Non-owning (inverse) | No                | No               | No (just reads, doesn't write FK)   | Uses FK column on owning side to fetch associated entities |

***

## Key Points

- You **must set** the owning side field (`Order.user`) to establish the relationship, not just the inverse side (`User.orders`).
- When persisting, JPA updates the foreign key column on the owning side's table.
- `mappedBy` tells JPA the inverse side doesn’t own the FK, preventing duplicate FK management or schema changes.
- Lazy fetching on `@OneToMany` results in additional queries for child collections.
***
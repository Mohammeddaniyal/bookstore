The change from default `FetchType.EAGER` to `FetchType.LAZY` on your `@ManyToOne` relationships like:

```java
@ManyToOne(fetch = FetchType.LAZY, optional = false)
private User user;

@ManyToOne(fetch = FetchType.LAZY, optional = false)
private Book book;
```

means the associated entities (`User` or `Book`) **will not be fetched immediately when you load the parent entity (e.g., Order or OrderItem)**. Instead, they will be fetched **only when you actually access those associations in your code**.

***

### Why this matters and what it means:

- **Default behavior (`EAGER`)**:  
  When you load an `Order` entity, JPA will immediately fetch the related `User` entity from the database at the same time—usually via a join or an extra query. Similarly for `OrderItem` → `Book`. This can cause **extra SQL queries or larger joins**, even if you don’t need these related entities in all scenarios.

- **With `FetchType.LAZY`**:  
  When you load an `Order`, JPA only fetches the order data **without** loading the associated `User`. Only when you call `order.getUser()` does it trigger a separate SQL query to fetch the user data from the database on demand.

***

### Benefits of using `FETCH.LAZY`:

- **Better performance** when you don't always need the related entities. You avoid unnecessary SQL joins or queries.
- **Lower memory consumption**, because fewer entities are loaded into memory initially.
- **More control** over when to access related data.

***

### Potential downside:

- You must be careful about the **Hibernate session scope** (or JPA EntityManager context). Lazy loading requires the session/entity manager to still be open when you access the lazy property, or you get a common error `LazyInitializationException` because the proxy cannot load the absent data once the session is closed.

***

### Summary:

| Feature           | FetchType.EAGER                           | FetchType.LAZY                            |
|-------------------|------------------------------------------|------------------------------------------|
| When does loading happen? | Immediately when parent entity is loaded | When associated property is accessed     |
| Number of SQL queries       | Fewer queries but potentially heavy joins or extra queries upfront | More queries (additional queries triggered on access) but lighter initial load |
| Performance impact           | Can cause unnecessary data loading and slow queries if associations are large or not needed | Loads data on demand; efficient if associated data is not always used |
| Risk of errors              | No lazy loading issues                     | Risk of `LazyInitializationException` if accessed outside session          |

***

### Why the suggested change?

By setting:

```java
@ManyToOne(fetch = FetchType.LAZY, optional = false)
private User user;
```

you are improving performance by deferring loading of `User` (and similarly `Book`) until required, which is usually beneficial in typical REST API applications where you carefully map entity data to DTOs and do not always need associated entities right away.

***

### Reference summary from sources:

- **FetchType.LAZY** means *“load on access”*, avoiding unnecessary overhead.[1][2][3][4]
- **FetchType.EAGER** fetches associations together during initial query, which may degrade performance if not always needed.[2][1]
- Best practice is usually to use **LAZY** on `@ManyToOne` and handle loading explicitly or via DTO mapping.[3][5]

***

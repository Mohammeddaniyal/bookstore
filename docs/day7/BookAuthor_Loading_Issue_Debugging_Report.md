***

# **Debugging Report – Book & Author Loading Issue**
**Date:** 08 Aug 2025  
**Author:** *Mohammed Daniyal*  
**Module:** BookService / Many‑to‑Many Book–Author Relationship (Hibernate + Spring Boot)

***

## **1. Problem Summary**
When performing a duplicate book check (same title + same authors), accessing the `authors` set from a `Book` entity caused:

- `ConcurrentModificationException` during iteration.
- `authors` set loading as empty despite correct database join table entries.
- `StackOverflowError` when switching between LAZY/EAGER fetch types.

***

## **2. Initial Hypotheses & Early Attempts**
We initially suspected Hibernate lazy‑loading pitfalls or concurrent modification of collections.

**Actions attempted:**
1. **Defensive Copy**
   ```java
   Set safeCopy = new HashSet<>(existingBook.getAuthors());
   ```
   → Helped in some cases, but failed if Hibernate modified the collection internally during initialization.

2. **Transactional Context & Forced Initialization**
   ```java
   @Transactional
   Hibernate.initialize(existingBook.getAuthors());
   ```
   → Ensured session was open, but issues persisted.

3. **Fetch Join Query**
   ```java
   @Query("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.authors WHERE b.title = :title")
   List findByTitleWithAuthors(String title);
   ```
   → Avoided lazy loading in theory, but authors still sometimes empty.

***

## **3. Key Observations from Debugging**
- **Database join table** `book_author` had correct data:
  ```
  book_id | author_id
      1   |     1
      1   |     2
  ```
- Hibernate executed correct SQL with joins, but collections still empty or causing exceptions.
- Using **Lombok `@Data`** on entities generated:
    - `equals()`
    - `hashCode()`
    - `toString()`
      → which included **bidirectional references** (`Book.authors` ↔ `Author.books`) → recursion.

***

## **4. Root Cause**
Lombok’s `@Data`:
- Generated `equals()` / `hashCode()` including the `authors` collection, which triggered:
    - **Infinite recursion** → `StackOverflowError`.
    - Collection access during Hibernate initialization → `ConcurrentModificationException`.
- Caused unexpected lazy‑loading behavior, making `authors` appear empty.

***

## **5. Final Resolution**
**Removed `@Data`** from both `Book` and `Author`.

Replaced with:
```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
```
(no `equals()`, `hashCode()`, or `toString()` auto‑generated).

**Result:**
- Authors load correctly (no empty set).
- LAZY and EAGER both work reliably.
- Works with `findByTitle()` or fetch‑join queries.
- No need for defensive copy or `Hibernate.initialize()`.

***

## **6. Lessons Learned / Best Practices**
- **Never** use Lombok’s `@Data` on JPA entities with bidirectional relationships.
- Avoid putting collections in `equals()` / `hashCode()` for entities.
- Use explicit getters/setters and implement equality based on immutable business keys when needed.
- Fetch join is still a good practice when you want authors with the book in a single query.
- Always confirm join table data before blaming fetch strategy.

***

## **7. Summary of What Worked**
✅ Removing `@Data` → **FIXED the issue** completely.  
✅ Both LAZY & EAGER now safe.  
✅ No special transaction or initialization tricks needed.

***

## **8. Keywords**
`Hibernate` · `Spring Boot` · `JPA` · `Lombok` · `@Data` · `ConcurrentModificationException` · `StackOverflowError` · `Lazy Loading` · `Bidirectional Mapping`

***

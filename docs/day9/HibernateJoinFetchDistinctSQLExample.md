# Hibernate SQL Output for JOIN FETCH Queries (With vs Without DISTINCT)

This document demonstrates the SQL Hibernate generates for a JPQL query using `JOIN FETCH`
on entity relationships, comparing the output **with** and **without** the `DISTINCT` keyword.  
Use this as a reference when testing locally to understand duplicate result issues.

***
Alright ✅ — let’s walk through the **exact SQL Hibernate will generate** in both cases so you can test it on your local database.

We’ll assume your JPQL query is like:
```java
@Query("SELECT o FROM Order o " +
       "JOIN FETCH o.orderItems oi " +
       "JOIN FETCH oi.book " +
       "WHERE o.user.email = :email " +
       "ORDER BY o.createdAt DESC")
```
…and the related entity mappings are:

```
Order
 └── orderItems (OneToMany)
        └── book (ManyToOne)
```

***

## **1️⃣ Without `DISTINCT`**
Hibernate will produce SQL similar to this *(table & column names depend on your mappings)*:

```sql
select
    o.id            as o_id,
    o.created_at    as o_created_at,
    o.updated_at    as o_updated_at,
    o.status        as o_status,
    o.total_amount  as o_total_amount,
    o.user_id       as o_user_id,
    
    oi.id           as oi_id,
    oi.order_id     as oi_order_id,
    oi.book_id      as oi_book_id,
    oi.quantity     as oi_quantity,
    oi.sub_total    as oi_sub_total,
    
    b.id            as b_id,
    b.title         as b_title,
    b.price         as b_price,
    b.quantity      as b_quantity
from
    orders o
    inner join order_items oi on o.id = oi.order_id
    inner join books b on oi.book_id = b.id
    inner join users u on o.user_id = u.id
where
    u.email = ?
order by
    o.created_at desc;
```

**➡ Issue:**  
If the order has multiple order items, you’ll get multiple rows with the **same `Order` columns repeated** (1 per order item).  
Hibernate will map **multiple `Order` objects** in your result list unless it deduplicates in memory — which it only does automatically when using `DISTINCT` in JPQL.

***

## **2️⃣ With `DISTINCT` in JPQL**
JPQL:
```java
@Query("SELECT DISTINCT o FROM Order o " +
       "JOIN FETCH o.orderItems oi " +
       "JOIN FETCH oi.book " +
       "WHERE o.user.email = :email " +
       "ORDER BY o.createdAt DESC")
```

Hibernate-generated SQL will look like:

```sql
select distinct
    o.id            as o_id,
    o.created_at    as o_created_at,
    o.updated_at    as o_updated_at,
    o.status        as o_status,
    o.total_amount  as o_total_amount,
    o.user_id       as o_user_id,
    
    oi.id           as oi_id,
    oi.order_id     as oi_order_id,
    oi.book_id      as oi_book_id,
    oi.quantity     as oi_quantity,
    oi.sub_total    as oi_sub_total,
    
    b.id            as b_id,
    b.title         as b_title,
    b.price         as b_price,
    b.quantity      as b_quantity
from
    orders o
    inner join order_items oi on o.id = oi.order_id
    inner join books b on oi.book_id = b.id
    inner join users u on o.user_id = u.id
where
    u.email = ?
order by
    o.created_at desc;
```

Only **difference in raw SQL**: `select distinct` instead of `select`.

The **real magic** happens inside Hibernate:
- With no `DISTINCT` → multiple `Order` objects in the Java list.
- With `DISTINCT` → Hibernate deduplicates the root `Order` entities in the returned list based on their primary key, even if SQL returns duplicates.

***

💡 **How to test locally:**
1. Enable SQL logging in `application.properties`:
   ```properties
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.format_sql=true
   ```
2. Run your method once **without `DISTINCT`** and once **with `DISTINCT`**.
3. Observe:
    - The SQL printed will be the same except for the `distinct` keyword.
    - Without `DISTINCT`, your result list in Java will likely contain repeated `Order` objects if you print them directly.

***

If you want, I can also **give you a quick test scenario with sample data** so when you run it, you can easily see the duplicate orders problem before `DISTINCT` and how it’s fixed after.  
Do you want me to prepare that test case for you?
***

## Improvements Resolved

### Expect Timestamps one all are resolved

## 🔹 Suggested Improvements in Entities

### **1. Add Proper `mappedBy` / Cascade / Orphan Settings**
You already use:
```java
@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
```
✅ This means deleting an `Order` will also delete its `OrderItem`s — correct for order systems.  
But in `Book`:
```java
@OneToMany(mappedBy = "book")
private List orderItems;
```
- This is fine for bidirectional mapping, but you might want `@JsonIgnore` here to **avoid infinite recursion** in serialization if you ever JSON-serialize `Book` → `OrderItem` → `Book`.
- Example:
```java
@OneToMany(mappedBy = "book")
@JsonIgnore
private List orderItems;
```

***

### **2. `User.roles` as a Separate Table**
Currently:
```java
@ElementCollection(fetch = FetchType.EAGER)
private List roles;
```
- This is fine, but **List** for roles allows duplicates; **Set** is usually better.
- Switch to:
```java
@ElementCollection(fetch = FetchType.EAGER)
private Set roles = new HashSet<>();
```

***

### **3. Price and Total Calculation Responsibility**
- You already have `Book.price`, `OrderItem.subTotal`, and `Order.totalAmount`.
- `subTotal` and `totalAmount` are **derived fields** (calculated from price × quantity).
- **Best practice**: Keep these fields in DB if you want to store historical price (important if price changes later).  
  So saving both `subTotal` and `totalAmount` is correct for audit/proof — ✅ no need to remove.

***

### **4. Timestamps**
In `Order`:
```java
@Column(nullable = false, updatable = false)
private LocalDateTime createdAt;
@Column(nullable = false)
private LocalDateTime updatedAt;
```
- Your `@PrePersist` & `@PreUpdate` is correct.
- If you plan to use auditing across multiple entities, consider Spring JPA **Auditing** (`@CreatedDate`, `@LastModifiedDate`) — less boilerplate.

***

### **5. Fetch Types**
- `@ManyToOne` **defaults to EAGER**, which may cause extra queries when loading lists of orders.
- For performance, consider:
```java
@ManyToOne(fetch = FetchType.LAZY, optional = false)
private User user;
```
Same for:
```java
@ManyToOne(fetch = FetchType.LAZY, optional = false)
private Book book;
```

***

## 🔹 DTO Layer Notes

Right now your `OrderItemDTO` is **used for both request & response**.  
It works, **but** since your entities have complex relations (`Book`, `Author`, etc.), here’s what I recommend:

***

### Option A — Keep As-Is (Simple Approach ✅)
If:
- Request needs only `bookId` and `quantity`
- Response only adds `subTotal` & maybe nested book info later  
  Then: Keep your `OrderItemDTO` as it is.

***

### Option B — Split into:
1. **OrderItemRequestDTO**  
   For placing/updating orders → only input fields.
2. **OrderItemResponseDTO**  
   For showing orders → includes `subTotal`, maybe book title, price.

Example:
```java
public class OrderItemRequestDTO {
    @NotNull private Long bookId;
    @Min(1) private int quantity;
}

public class OrderItemResponseDTO {
    private Long bookId;
    private String title;
    private int quantity;
    private BigDecimal subTotal;
    private BigDecimal price; // optional
}
```
💡 This prevents exposing fields in requests that shouldn’t be changeable by the client.

***

## 🔹 Final Refactor Suggestions Summary

### **Entities:**
- Change `roles` to `Set`
- Add `@JsonIgnore` to `Book.orderItems`
- Consider `fetch = LAZY` for `@ManyToOne` in `Order` and `OrderItem`
- Keep `subTotal` and `totalAmount` for price history — ✅ correct.
- Optionally replace `@PrePersist/@PreUpdate` with Spring Auditing for consistency.

***

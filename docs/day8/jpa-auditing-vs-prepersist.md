Spring Data JPA Auditing is a convenient feature that automates the population of auditing fields like creation and modification timestamps across multiple entities with minimal boilerplate.

### How to Enable Spring JPA Auditing

1. **Add configuration to enable auditing**  
   In your Spring Boot main application class or a configuration class, add the annotation:
   ```java
   @EnableJpaAuditing
   @SpringBootApplication
   public class YourApplication {
       public static void main(String[] args) {
           SpringApplication.run(YourApplication.class, args);
       }
   }
   ```

2. **Annotate entity fields for auditing**  
   In your entity class, replace your manual `@PrePersist` and `@PreUpdate` methods with fields annotated as:

    - `@CreatedDate` for creation timestamp
    - `@LastModifiedDate` for last update timestamp

   Example for your `Order` entity fields:
   ```java
   @Entity
   public class Order {

       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long id;

       @CreatedDate
       @Column(nullable = false, updatable = false)
       private LocalDateTime createdAt;

       @LastModifiedDate
       @Column(nullable = false)
       private LocalDateTime updatedAt;

       // other fields and mappings...
   }
   ```

3. **Add auditing auditor aware bean (optional for user auditing)**  
   If you want to also track who created or modified the entity, you add `@CreatedBy` and `@LastModifiedBy` and provide an `AuditorAware` bean. For timestamps only, this is optional.

4. **Enable JPA auditing in entities**  
   Annotate your entity class or a base class with:
   ```java
   @EntityListeners(AuditingEntityListener.class)
   public class Order {
       // ...
   }
   ```

### Key Differences Between Manual `@PrePersist/@PreUpdate` and Spring JPA Auditing

| Aspect                        | Manual `@PrePersist`/`@PreUpdate`                 | Spring Data JPA Auditing                                   |
|-------------------------------|--------------------------------------------------|------------------------------------------------------------|
| Setup                        | Must write lifecycle methods in every entity     | Enable globally with `@EnableJpaAuditing` once             |
| Boilerplate                  | More code per entity                              | Minimal; annotation-based                                  |
| Automatic timestamp updates   | You control when and how timestamps are set      | Automatically handled by the framework                      |
| Support for createdBy/modifiedBy | Requires manual coding                            | Supports user auditing with `AuditorAware` implementation  |
| Consistency                  | Risk of inconsistent implementations among entities | Consistent across all audited entities                      |
| Reusability                  | Less reusable unless extracted to a base class   | Can be combined with base classes or global configuration   |

### Summary

- Using Spring JPA Auditing eliminates the need for manual timestamp management via `@PrePersist` and `@PreUpdate`.
- You only need to annotate fields with `@CreatedDate` and `@LastModifiedDate`, add `@EntityListeners(AuditingEntityListener.class)`, and enable auditing once with `@EnableJpaAuditing`.
- It's cleaner and easier for multi-entity projects.
- Optional support for tracking who created or last modified entities.

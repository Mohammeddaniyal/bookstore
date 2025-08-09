## Addition: Book and Author Modules Covered

### 1. Book and Author Entities
- Defined entities for `Book` and `Author` with appropriate relationships.
- Included fields like title, description, author details, and other domain-relevant attributes.

### 2. Repository Layer
- Created Spring Data JPA repositories (`BookRepository`, `AuthorRepository`) for database CRUD operations.

### 3. Service Layer
- Developed service classes to handle business logic for books and authors.
- Implemented CRUD operations, search or filtering where applicable.

### 4. REST Controllers
- Created REST APIs exposing endpoints for:
    - Adding, updating, deleting books and authors.
    - Retrieving book/author lists and individual details.
- Secured these endpoints so that only users with the `ADMIN` role can modify book and author data.

### 5. Security Configuration
- Updated security rules to allow public registration and login.
- Allowed only `ADMIN` role to access `/api/books/**` and `/api/authors/**`.
- The other roles are restricted from these endpoints as appropriate.

### 6. Documentation File Created
- We prepared a documentation file summarizing all the key concepts, designs, and solutions including:
    - JWT Exception handling approach
    - Security configuration changes
    - Book & Author module design
    - Order & OrderItem planning and APIs
***

### Questions and Doubts from Today

1. **Book Update Best Practices**
    - How to implement the updateBook method with checks for existence, duplicates, and ISBN immutability?
    - Should the update service throw exceptions or return Optional?
    - Is validation for all fields needed when handling partial updates?

2. **Handling Immutability**
    - How do I enforce that ISBN is immutable in full and partial updates?
    - Why is making the ISBN field immutable so important, both in this project and in real-world applications?
    - When should a field be considered for immutability in general?

3. **Partial Update (PATCH) Concepts**
    - What is the difference between PATCH and PUT? Can I support both?
    - How to design a PATCH endpoint and BookUpdateDTO for partial updates?
    - Can unknown fields (like isbn in PATCH if not in DTO) be sent, and what happens to them?
    - Can BookRequestDTO be reused for PATCH if I skip @Valid?

4. **Validation, DTO Structure, and Error Handling**
    - How to structure full update vs partial update DTOs?
    - Should I document immutable fields in the API or also enforce them with code?
    - How to return meaningful validation error responses for all scenarios?
    - Should the service check for duplicate ISBN or rely on the database?
    - How do I deal with save() returning null—should I raise an exception, and what status code is appropriate?

5. **API Status Codes and Design**
    - What HTTP status codes should be returned for success, validation failure, duplicate, forbidden, not found, and persistence errors?
    - Is it okay to send a response with an empty errors map for business rule violations?

6. **Testing and Implementation Approach**
    - Is automated testing (unit/integration) necessary, or is Postman/manual testing sufficient?
    - Should I favor controller-level or service-level tests given my use case and style?
    - What’s the impact of the Mockito inline mock-maker warning?

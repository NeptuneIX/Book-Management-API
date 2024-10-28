# Book-Management-API
This is a REST API project I made using Spring Boot.

Welcome to the Book Management API!

**Available Endpoints:**
**Books:**
1. GET /books - Get all books
2. GET /books/{id} - Get book by ID
3. GET /books/{id}/borrowers - Get borrowers of a book
4. POST /books - Add a new book
5. DELETE /books/{id} - Delete a book by ID
6. PUT /books/{id} - Update available copies of a book

**Borrowers:**
1. GET /borrowers - Get all borrowers
2. GET /borrowers/{id} - Get borrower by ID
3. GET /borrowers/{id}/books - Get books borrowed by a borrower
4. POST /borrowers - Add a new borrower
5. DELETE /borrowers/{id} - Delete a borrower by ID
6. PUT /borrowers/{borrowerId}/books/{bookId} - Add a book to a borrower's list

**Features:**
1. **View all books and borrowers**: Retrieve all book and borrower records stored in the database.
2. **View details of a specific book or borrower**: Fetch details of a book or borrower by their ID.
3. **View borrowers of a specific book and vice versa**: See which borrowers have borrowed a particular book and which books a borrower has borrowed.
4. **Add new books and borrowers**: Create new book and borrower records.
5. **Update the available copies of a book**: Increment or decrement the count of available book copies.
6. **Delete books and borrowers**: Remove book and borrower records from the database.

**Technical Features:**
1. **Exception Handling**: Custom exceptions like `ResourceNotFoundException` and `EntityExistsException` ensure proper error handling and clear messaging.
2. **Database Management**: Utilizes Spring Data JPA for efficient database operations with repositories for `Book` and `Borrower` entities.
3. **Validation**: Uses Jakarta Bean Validation to ensure that entity constraints are met before database operations.
4. **Hypermedia as the Engine of Application State (HATEOAS)**: Provides hypermedia links to navigate the API, enhancing discoverability and client usability.
5. **RESTful Design**: Follows REST principles for resource management, enabling easy integration and interaction with the API.
6. **Versioning**: API versioning for backward compatibility and iterative improvement.
7. **Swagger/OpenAPI Integration**: API documentation is accessible via Swagger UI for interactive exploration and testing.
8. **Spring Boot Actuator**: Provides production-ready features like monitoring and management.
9. **Development Tools**: Spring Boot DevTools for live reload during development, enhancing developer productivity.





Video overview: https://youtu.be/g1gg6iNEiWY

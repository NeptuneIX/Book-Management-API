package com.myprojects.bookmanagementapi.restfulwebservices;


import com.myprojects.bookmanagementapi.entities.Book;
import com.myprojects.bookmanagementapi.entities.Borrower;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@RestController
public class ManagementService {

    private BookRepository bookRepository;
    private BorrowerRepository borrowerRepository;

    public ManagementService(BookRepository bookRepository, BorrowerRepository borrowerRepository) {
        this.bookRepository = bookRepository;
        this.borrowerRepository = borrowerRepository;
    }

    // Default API information (version 1)

    @GetMapping(path ="/")
    public ResponseEntity<?> getApiInformation() {

        String apiInfo = "Welcome to the Book Management API!\n\n" +
                "**Available Endpoints:**\n" +
                "**Books:**\n" +
                "1. GET /books - Get all books\n" +
                "2. GET /books/{id} - Get book by ID\n" +
                "3. GET /books/{id}/borrowers - Get borrowers of a book\n" +
                "4. POST /books - Add a new book\n" +
                "5. DELETE /books/{id} - Delete a book by ID\n" +
                "6. PUT /books/{id} - Update available copies of a book\n\n" +
                "**Borrowers:**\n" +
                "1. GET /borrowers - Get all borrowers\n" +
                "2. GET /borrowers/{id} - Get borrower by ID\n" +
                "3. GET /borrowers/{id}/books - Get books borrowed by a borrower\n" +
                "4. POST /borrowers - Add a new borrower\n" +
                "5. DELETE /borrowers/{id} - Delete a borrower by ID\n" +
                "6. PUT /borrowers/{borrowerId}/books/{bookId} - Add a book to a borrower's list\n\n" +
                "**Features:**\n" +
                "1. **View all books and borrowers**: Retrieve all book and borrower records stored in the database.\n" +
                "2. **View details of a specific book or borrower**: Fetch details of a book or borrower by their ID.\n" +
                "3. **View borrowers of a specific book and vice versa**: See which borrowers have borrowed a particular book and which books a borrower has borrowed.\n" +
                "4. **Add new books and borrowers**: Create new book and borrower records.\n" +
                "5. **Update the available copies of a book**: Increment or decrement the count of available book copies.\n" +
                "6. **Delete books and borrowers**: Remove book and borrower records from the database.\n\n" +
                "**Technical Features:**\n" +
                "1. **Exception Handling**: Custom exceptions like `ResourceNotFoundException` and `EntityExistsException` ensure proper error handling and clear messaging.\n" +
                "2. **Database Management**: Utilizes Spring Data JPA for efficient database operations with repositories for `Book` and `Borrower` entities.\n" +
                "3. **Validation**: Uses Jakarta Bean Validation to ensure that entity constraints are met before database operations.\n" +
                "4. **Hypermedia as the Engine of Application State (HATEOAS)**: Provides hypermedia links to navigate the API, enhancing discoverability and client usability.\n" +
                "5. **RESTful Design**: Follows REST principles for resource management, enabling easy integration and interaction with the API.\n" +
                "6. **Versioning**: API versioning for backward compatibility and iterative improvement.\n" +
                "7. **Swagger/OpenAPI Integration**: API documentation is accessible via Swagger UI for interactive exploration and testing.\n" +
                "8. **Spring Boot Actuator**: Provides production-ready features like monitoring and management.\n" +
                "9. **Development Tools**: Spring Boot DevTools for live reload during development, enhancing developer productivity.\n";

        return ResponseEntity.ok(apiInfo);
    }

    // API Information version 1
    @GetMapping(path ="/", headers = "X-API-VERSION=1")
    public ResponseEntity<?> getApiInformationLong() {
        return getApiInformation();
    }

    // API Information version 2
    @GetMapping(path = "/", headers = "X-API-VERSION=2")
    public ResponseEntity<?> getApiInformationShort() {

        String apiInfo = """
            Welcome to the Book Management API!

            Available Endpoints:
            - Books: GET /books, GET /books/{id}, GET /books/{id}/borrowers, POST /books, DELETE /books/{id}, PUT /books/{id}
            - Borrowers: GET /borrowers, GET /borrowers/{id}, GET /borrowers/{id}/books, POST /borrowers, DELETE /borrowers/{id}, PUT /borrowers/{borrowerId}/books/{bookId}

            Technical Features:
            - Exception Handling: Custom exceptions for clear error messages
            - Database Management: Spring Data JPA for database operations
            - Validation: Ensures entity constraints before database operations
            - HATEOAS: Hypermedia links for API navigation
            - RESTful Design: Follows REST principles for resource management
            - Versioning (Future): Plans to support API versioning
            """;

        return ResponseEntity.ok(apiInfo);
    }


    // Book requests

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/books/{id}")
    public EntityModel<Book> getBook(@PathVariable Integer id) {
        Optional<Book> book = bookRepository.findById(id);
        if(book.isEmpty()) throw new ResourceNotFoundException("Book not found");

        // So that we can add a link header to another url on top of the data of user that we give back, we use the EntityModel class
        EntityModel<Book> model = EntityModel.of(book.get());

        // with linkTo we link to another url, by first stating that it's a method on this class, specifically getAllUsers.
        WebMvcLinkBuilder link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllBooks());
        model.add(link.withRel("all-books"));
        return model;
    }

    @GetMapping("/books/{id}/borrowers")
    public List<EntityModel<Borrower>> retrieveBookBorrowerList(@PathVariable Integer id) {

        Optional<Book> book = bookRepository.findById(id);
        if(book.isEmpty()) throw new ResourceNotFoundException("Book not found");

        // Retrieve the list
        List<Borrower> borrowerList = book.get().getBorrowerList();

        List<EntityModel<Borrower>> borrowerListWithLinks = new ArrayList<>();
        // For every borrower in the list, create an EntityModel version of it that has a link to its SPECIFIC get request, depending on its ID
        for(Borrower borrower : borrowerList) {
            EntityModel<Borrower> borrowerElement = EntityModel.of(borrower);
            WebMvcLinkBuilder builder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getBorrower(borrower.getId()));
            borrowerElement.add(builder.withRel("book"));

            // Add each wrapped book element to the list
            borrowerListWithLinks.add(borrowerElement);
        }

        return borrowerListWithLinks;
    }

    @PostMapping("/books")
    public Book addBook(@Valid @RequestBody Book book) {
        return bookRepository.save(book);
    }

    @DeleteMapping("/books/{id}")
    public EntityModel<Book> deleteBook(@PathVariable Integer id) {
        Optional<Book> savedBook = bookRepository.findById(id);

        if(savedBook.isEmpty()) throw new ResourceNotFoundException("Book not found");

        bookRepository.deleteById(id);

        // Must wrap in EntityModel and return it as that, because if we return borrower.get() it will give an error because borrower will already be deleted by then
        EntityModel<Book> bookResponseEntity = EntityModel.of(savedBook.get());

        // Add a link header for getting all borrowers
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllBooks());

        bookResponseEntity.add(linkBuilder.withRel("all-borrowers"));

        borrowerRepository.deleteById(id);

        return bookResponseEntity;

    }


    // Can improve this by returning some http status error when available copies are already 0 when we try to decrement
    @PutMapping("/books/{id}")
    public EntityModel<Book> updateAvailableBookCopies(@PathVariable Integer id, @RequestBody Map<String, Object> book) throws Exception{
        Optional<Book> savedBook = bookRepository.findById(id);

        if(savedBook.isEmpty()) throw new ResourceNotFoundException("Book not found");


        // We should receive an "action" field in the req body, this tells us whether to increment or decrement
        String action = ((String) book.get("action")).toLowerCase();

        if(!savedBook.get().isAvailable() && action.equals("decrement")) throw new Exception("Cannot decrement available copies below 0");

        // If we receive increment, then pass on true, otherwise just false
        savedBook.get().updateAvailableCopies(action.equals("increment"));

        // Save & overwrite changes to the repository
        bookRepository.save(savedBook.get());

        EntityModel<Book> bookResponseEntity = EntityModel.of(savedBook.get());


        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllBooks());

        bookResponseEntity.add(linkBuilder.withRel("all-borrowers"));


        return bookResponseEntity;
    }





    // Borrower requests

    @GetMapping("/borrowers")
    public List<Borrower> getAllBorrowers() {
        return borrowerRepository.findAll();
    }

    @GetMapping("/borrowers/{id}")
    public EntityModel<Borrower> getBorrower(@PathVariable Integer id) {
        Optional<Borrower> borrower = borrowerRepository.findById(id);
        if(borrower.isEmpty()) throw new ResourceNotFoundException("Borrower not found");

        EntityModel<Borrower> model = EntityModel.of(borrower.get());
        WebMvcLinkBuilder link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllBorrowers());
        model.add(link.withRel("all-borrowers"));

        return model;
    }


    @GetMapping("/borrowers/{id}/books")
    public List<EntityModel<Book>> retrieveBorrowerBooksList(@PathVariable Integer id) {
        Optional<Borrower> borrower = borrowerRepository.findById(id);

        if(borrower.isEmpty()) throw new ResourceNotFoundException("Borrower not found");

        // Retrieve the list
        List<Book> borrowedBooks = borrower.get().getBorrowedBooks();

        List<EntityModel<Book>> borrowedBooksWithLinks = new ArrayList<>();
        // For every book in the list, create an EntityModel version of it that has a link to its SPECIFIC get request, depending on its ID
        for(Book book : borrowedBooks) {
            EntityModel<Book> bookElement = EntityModel.of(book);
            WebMvcLinkBuilder builder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getBook(book.getId()));
            bookElement.add(builder.withRel("book"));

            // Add each wrapped book element to the list
            borrowedBooksWithLinks.add(bookElement);
        }

        return borrowedBooksWithLinks;
    }

    @PostMapping("/borrowers")
    public Borrower addBorrower(@Valid @RequestBody Borrower borrower) {
        return borrowerRepository.save(borrower);
    }

    @DeleteMapping("/borrowers/{id}")
    public EntityModel<Borrower> deleteBorrower(@PathVariable Integer id) {
        Optional<Borrower> borrower = borrowerRepository.findById(id);

        if(borrower.isEmpty()) throw new ResourceNotFoundException("Borrower not found");

        // Must wrap in EntityModel and return it as that, because if we return borrower.get() it will give an error because borrower will already be deleted by then
        EntityModel<Borrower> borrowerResponseEntity = EntityModel.of(borrower.get());

        // Add a link header for getting all borrowers
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllBorrowers());

        borrowerResponseEntity.add(linkBuilder.withRel("all-borrowers"));

        borrowerRepository.deleteById(id);

        return borrowerResponseEntity;

    }

    @PutMapping("/borrowers/{borrowerId}/books/{bookId}")
    public EntityModel<Borrower> addBookToBorrower(@PathVariable Integer borrowerId, @PathVariable Integer bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        Optional<Borrower> borrower = borrowerRepository.findById(borrowerId);

        if(book.isEmpty() && borrower.isEmpty()) throw new ResourceNotFoundException("Book and borrower not found");

        if(borrower.isEmpty()) throw new ResourceNotFoundException("Borrower not found");
        if(book.isEmpty()) throw new ResourceNotFoundException("Book not found");


        // Many to many entity relationship means many book entities can have a relationship to many borrower entities

        // Checks for the existence of a book in a borrower's book list, returns a bool.
        if(!borrower.get().updateBorrowedBooks(book.get())) throw new EntityExistsException("Book already exists in borrower's book list");
        // This does the same, but just returns void
        book.get().updateBorrowerList(borrower.get());

        borrowerRepository.save(borrower.get());
        bookRepository.save(book.get());

        // Again, we wrap Borrower with "EntityModel" and construct a link header to the getAllBorrowers URL & add it to the wrapper EntityModel
        EntityModel<Borrower> borrowerResponseEntity = EntityModel.of(borrower.get());

        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllBorrowers());

        borrowerResponseEntity.add(linkBuilder.withRel("all-borrowers"));

        return borrowerResponseEntity;
    }


    



}

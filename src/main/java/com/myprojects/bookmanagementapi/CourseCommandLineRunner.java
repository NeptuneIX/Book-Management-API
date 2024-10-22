package com.myprojects.bookmanagementapi;

import com.myprojects.bookmanagementapi.entities.Book;
import com.myprojects.bookmanagementapi.entities.Borrower;
import com.myprojects.bookmanagementapi.restfulwebservices.BookRepository;
import com.myprojects.bookmanagementapi.restfulwebservices.BorrowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CourseCommandLineRunner implements CommandLineRunner {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BorrowerRepository borrowerRepository;

    @Override
    public void run(String... args) throws Exception {
        bookRepository.save(new Book("Hemming", "Douglas Mack", true, 4));
        bookRepository.save(new Book("Daniel's Dream", "Jeff Mackinson", true, 2));

        borrowerRepository.save(new Borrower("Johnny", "johnny223@gmail.com"));
        borrowerRepository.save(new Borrower("Danny", "dannytwooes100@outlook.com"));
    }
}

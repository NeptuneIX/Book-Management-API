package com.myprojects.bookmanagementapi.restfulwebservices;

import com.myprojects.bookmanagementapi.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
}

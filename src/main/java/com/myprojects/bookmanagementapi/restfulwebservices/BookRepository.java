package com.myprojects.bookmanagementapi.restfulwebservices;

import com.myprojects.bookmanagementapi.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
}

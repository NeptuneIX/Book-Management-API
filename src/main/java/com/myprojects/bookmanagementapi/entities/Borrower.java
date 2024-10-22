package com.myprojects.bookmanagementapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
public class Borrower {

    @Id
    @GeneratedValue
    private Integer id;


    @NotNull
    private String name;
    @NotNull
    private String email;

    @ManyToMany
    private List<Book> borrowedBooks;

    public Borrower() {}


    public Borrower(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public boolean updateBorrowedBooks(Book borrowedBook) {
        if(this.borrowedBooks.contains(borrowedBook)) {
            return false;
        }

        this.borrowedBooks.add(borrowedBook);

        return true;
    }
}

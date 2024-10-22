package com.myprojects.bookmanagementapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class Book {
    @Id
    @GeneratedValue
    private Integer id;

    @Size(min = 2, max = 50)
    private String title;
    @Size(min = 2, max = 50)
    private String author;


    private boolean available = true;

    @NotNull
    private Integer availableCopies;

    @ManyToMany(mappedBy = "borrowedBooks")
    @JsonIgnore
    private List<Borrower> borrowerList;

    public Book() {}


    public Book(String title, String author, boolean available, Integer availableCopies) {

        this.title = title;
        this.author = author;
        this.available = available;
        this.availableCopies = availableCopies;
    }

    public List<Borrower> getBorrowerList() {
        return borrowerList;
    }

    public void setBorrowerList(List<Borrower> borrowerList) {
        this.borrowerList = borrowerList;
    }

    public void updateBorrowerList(Borrower borrower) {
        this.borrowerList.add(borrower);
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }

    public void updateAvailableCopies(boolean increment) {
        if(increment) {
            if(this.availableCopies == 0) this.available = true;
            this.availableCopies += 1;
        }
        if(!increment && this.availableCopies != 0){
            if(this.availableCopies == 1) this.available = false;
            this.availableCopies -= 1;
        }
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}

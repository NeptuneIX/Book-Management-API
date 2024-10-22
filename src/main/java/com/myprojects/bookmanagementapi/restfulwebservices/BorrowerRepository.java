package com.myprojects.bookmanagementapi.restfulwebservices;

import com.myprojects.bookmanagementapi.entities.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, Integer> {
}

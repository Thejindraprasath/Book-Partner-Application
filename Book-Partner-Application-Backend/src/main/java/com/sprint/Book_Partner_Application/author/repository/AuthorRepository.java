package com.sprint.Book_Partner_Application.author.repository;



import com.sprint.Book_Partner_Application.author.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, String> {

    Page<Author> findByCityIgnoreCase(String city, Pageable pageable);

    Page<Author> findByStateIgnoreCase(String state, Pageable pageable);

    Page<Author> findByContract(int contract, Pageable pageable);

    @Query("SELECT a FROM Author a")
    Page<Author> findWithFilters(Pageable pageable);

    List<Author> findByAuLnameContainingIgnoreCaseOrAuFnameContainingIgnoreCase(String lname, String fname);
}
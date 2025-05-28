package com.example.library_system.repository;

import com.example.library_system.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorFirstNameContainingIgnoreCaseOrAuthorLastNameContainingIgnoreCase
            (String firstName, String lastName);
    Optional<Book> findById(Long bookId);

}
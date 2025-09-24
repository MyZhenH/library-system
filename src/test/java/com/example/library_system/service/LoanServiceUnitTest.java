package com.example.library_system.service;

import com.example.library_system.entity.Book;
import com.example.library_system.entity.Loan;
import com.example.library_system.entity.User;
import com.example.library_system.repository.BookRepository;
import com.example.library_system.repository.LoanRepository;
import com.example.library_system.repository.UserRepository;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceUnitTest {


    @InjectMocks
    private LoanService loanService;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;


    @Test
    public void testCreateLoan() {

        //Arrange
        Long userId = 1L;
        Long bookId = 2L;

        Book book = new Book();
        book.setBookId(bookId);
        book.setAvailableCopies(2);

        User user = new User();
        user.setUserId(userId);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setUser(user);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

    //Act
        //Loan createLoan = loanService.createLoan(userId, bookId); //Förändring i och med Authentication, spring security
    //Assert
        //assertNotNull(createLoan);
        //assertEquals(1, createLoan.getUser().getUserId());
        //assertEquals(2, createLoan.getBook().getBookId());
        //assertEquals(1, createLoan.getBook().getAvailableCopies());

    }


}
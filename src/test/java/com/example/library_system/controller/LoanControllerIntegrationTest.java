package com.example.library_system.controller;

import com.example.library_system.entity.Book;
import com.example.library_system.entity.Loan;
import com.example.library_system.entity.User;
import com.example.library_system.repository.BookRepository;
import com.example.library_system.repository.LoanRepository;
import com.example.library_system.repository.UserRepository;
import com.example.library_system.service.LoanService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LoanControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanService loanService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void testCreateLoan() throws Exception{

        //arrange
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setEmail("email@test.com");
        user.setPassword("password");
        user.setRegistrationDate(LocalDateTime.now());
        userRepository.save(user);

        Book book = new Book();
        book.setTitle("Test");
        book.setPublicationYear(2025);
        book.setAvailableCopies(3);
        book.setTotalCopies(3);
        bookRepository.save(book);

        Loan loan = new Loan();
        loan.setBorrowedDate(LocalDateTime.now());
        loan.setDueDate(LocalDateTime.now().plusDays(14));

        //Alternativ 1
        //LocalDateTime borrowedDate = loan.getBorrowedDate();
        //LocalDateTime loanDueDate = borrowedDate.plusDays(14);

        //Alternativ 3
        LocalDateTime dueDate = LocalDateTime.now().plusDays(14);

        //act
        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("userId", user.getUserId().toString())
                                .param("bookId", book.getBookId().toString()))
                                .andExpect(status().isCreated());
        //assert
                        //Kontrollera att bokexemplar minskar vid lån
                        Book bookUpdate = bookRepository.findById(book.getBookId()).orElseThrow();
                        assertEquals(2, bookUpdate.getAvailableCopies());

                        //Kontrollera att återlämningsdag stämmer
                        //assertEquals(loan.getDueDate(), loanDueDate); //Alternativ 1
                        //assertEquals(loan.getDueDate(), loan.getBorrowedDate().plusDays(14)); //Alternativ 2
                        assertEquals(loan.getDueDate(), dueDate); //Alternativ 3

    }



}
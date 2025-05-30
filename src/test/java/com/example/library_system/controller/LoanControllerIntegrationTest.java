package com.example.library_system.controller;

import com.example.library_system.entity.Book;
import com.example.library_system.entity.User;
import com.example.library_system.repository.BookRepository;
import com.example.library_system.repository.LoanRepository;
import com.example.library_system.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        userRepository.save(user);

        Book book = new Book();
        book.setTitle("Test");
        book.setPublicationYear(2025);
        book.setAvailableCopies(3);
        book.setTotalCopies(3);
        bookRepository.save(book);

        Book bookUpdate = bookRepository.findById(book.getBookId()).orElseThrow();

        //act
        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("userId=" + user.getUserId() + "&" + "bookId=" + book.getBookId()))
        //assert
                        .andExpect(status().isCreated());
                        assertEquals(2, bookUpdate.getAvailableCopies());

    }



}
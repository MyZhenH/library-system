package com.example.library_system.service;

import com.example.library_system.entity.Book;
import com.example.library_system.entity.Loan;
import com.example.library_system.entity.User;
import com.example.library_system.repository.BookRepository;
import com.example.library_system.repository.LoanRepository;
import com.example.library_system.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public List<Loan> getAllLoansByUserId(Long userId){
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found.");
        }
        return loanRepository.findByUserId(userId);
    }

    @Transactional
    public Loan createLoan(Long userId, Long bookId) {

        //User
       Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
        User user = userOptional.get();

        //Book
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if(bookOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found.");
        }
        Book book = bookOptional.get();

        if(book.getAvailableCopies() <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no copies available");
        }

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setBorrowedDate(LocalDateTime.now().withNano(0));
        loan.setDueDate(LocalDateTime.now().plusDays(14).withNano(0));
        loanRepository.save(loan);

        //Minska bokexemplar
        book.setAvailableCopies(book.getAvailableCopies() -1);
        bookRepository.save(book);

        return loan;

    }

    @Transactional
    public Loan returnLoan(Long loanId){
      Optional<Loan> loanOptional = loanRepository.findById(loanId);
        if (loanOptional.isEmpty()) {
            throw new IllegalArgumentException("Loan not found.");
        }
        Loan loan = loanOptional.get();

        if(loan.getReturnedDate() != null){
            throw new IllegalArgumentException("Book already returned");
        }
        loan.setReturnedDate(LocalDateTime.now().withNano(0));
        loanRepository.save(loan);

        Long bookId = loan.getBook().getBookId();
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (bookOptional.isEmpty()) {
            throw new IllegalArgumentException("Book not found");
        }

        Book book = bookOptional.get();

        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return loan;

    }

    public Loan extendLoan(Long loanId){
      Optional<Loan> loanOptional = loanRepository.findById(loanId);

      if(loanOptional.isEmpty()){
          throw new IllegalArgumentException("Loan not found");
        }
        Loan loan = loanOptional.get();

        if(loan.getReturnedDate() != null){
            throw new IllegalArgumentException("Book already returned");
        }

        if(loan.getDueDate().isBefore(LocalDateTime.now())){
          throw new IllegalArgumentException("Loan cannot extend due to overdue");
        }

        loan.setDueDate(loan.getDueDate().plusDays(14));
        return loanRepository.save(loan);

    }


}

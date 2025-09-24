package com.example.library_system.service;

import com.example.library_system.entity.Book;
import com.example.library_system.entity.Loan;
import com.example.library_system.entity.User;
import com.example.library_system.exception.*;
import com.example.library_system.repository.BookRepository;
import com.example.library_system.repository.LoanRepository;
import com.example.library_system.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    SecurityService securityService;

    @Autowired
    public LoanService(LoanRepository loanRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public List<Loan> getAllLoansByUserId(Long userId, Authentication authentication){
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found.");
        }
        //Validera att användaren har bara tillgång till sin lån
        if (!securityService.hasAccessToLoan(userId, authentication)) {
            throw new ForbiddenException("Unauthorized");
        }
        return loanRepository.findByUserId(userId);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_USER')")
    public Loan createLoan(Long bookId, Authentication authentication) {

        //Hämta inloggade användaren
        String email = authentication.getName();

        User user = userRepository.findByEmailContainingIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        //Hämta boken
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found."));

        //Kontrollera tillgängliga bokexemplar
        if (book.getAvailableCopies() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No copies available.");
        }

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setBorrowedDate(LocalDateTime.now().withNano(0));
        loan.setDueDate(LocalDateTime.now().plusDays(14).withNano(0));
        loanRepository.save(loan);

        //Uppdatera tillgängliga bokexemplar
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        return loan;
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Loan returnLoan(Long loanId, Authentication authentication) {
        String email = authentication.getName();

        // Hämta inloggad användare
        User user = userRepository.findByEmailContainingIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Hämta lånet
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));

        // Kontrollera behörighet
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !loan.getUser().getUserId().equals(user.getUserId())) {
            throw new ForbiddenException("Unauthorized");
        }

        if(loan.getReturnedDate() != null){
            throw new BadRequestException("Book already returned");
        }
        loan.setReturnedDate(LocalDateTime.now().withNano(0));
        loanRepository.save(loan);

        //Bok
        Long bookId = loan.getBook().getBookId();
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (bookOptional.isEmpty()) {
            throw new BookNotFoundException("Book not found");
        }

        Book book = bookOptional.get();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return loan;

    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Loan extendLoan(Long loanId, Authentication authentication){
        String email = authentication.getName();

        // Hämta inloggad användare
        User user = userRepository.findByEmailContainingIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Hämta lånet
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));

        // Kontrollera behörighet
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !loan.getUser().getUserId().equals(user.getUserId())) {
            throw new ForbiddenException("Unauthorized");
        }

        if(loan.getReturnedDate() != null){
            throw new BadRequestException("Book already returned");
        }

        if(loan.getDueDate().isBefore(LocalDateTime.now())){
          throw new BadRequestException("Loan cannot extend due to overdue");
        }

        loan.setDueDate(loan.getDueDate().plusDays(14));
        return loanRepository.save(loan);

    }


}

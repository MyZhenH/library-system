package com.example.library_system.controller;

import com.example.library_system.entity.Loan;
import com.example.library_system.service.LoanService;
import com.example.library_system.service.SecurityService;
import com.example.library_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/")
public class LoanController {
    private final LoanService loanService;
    private final UserService userService;

    @Autowired
    public LoanController(LoanService loanService, UserService userService) {
        this.loanService = loanService;
        this.userService = userService;
    }


    //Hämta användarens lån
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/users/{userId}/loans")
    public ResponseEntity<List<Loan>> getAllLoansByUserId(@PathVariable Long userId, Authentication authentication){

        List<Loan> loansList = loanService.getAllLoansByUserId(userId, authentication);
        if(loansList.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(loansList);
    }

    //Låna bok
    @PostMapping("/loan")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Loan> createLoan(@RequestParam Long bookId, Authentication authentication) {

        Loan loan = loanService.createLoan(bookId, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }


    //Lämna tillbaks lån
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/loans/{loanId}/return")
    public ResponseEntity<Loan> returnLoan(@PathVariable Long loanId, Authentication authentication) {

        Loan loan = loanService.returnLoan(loanId, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(loan);

    }

    //Förnya lån
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/loans/{loanId}/extend")
    public ResponseEntity<Loan> extendLoan(@PathVariable Long loanId, Authentication authentication){

        Loan loan = loanService.extendLoan(loanId, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(loan);
    }





    //*********** Tidigare kod (innan säkerhet) **********

    //Gammla koden
    //@PreAuthorize("hasRole('ROLE_USER')")
    //@PostMapping("/loan")
    //public ResponseEntity<Loan> createLoan(@RequestParam Long userId, @RequestParam Long bookId){

    //  Loan loan = loanService.createLoan(userId, bookId);
    //return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    //}

    //Gamla koden
    //@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    //@PutMapping("/loans/{loanId}/return")
    //public ResponseEntity<Loan> returnLoan(@PathVariable Long loanId){

        //Loan loan = loanService.returnLoan(loanId);
        //return ResponseEntity.status(HttpStatus.OK).body(loan);
    //}

}

package com.example.library_system.controller;

import com.example.library_system.entity.Loan;
import com.example.library_system.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class LoanController {
    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("/users/{userId}/loans")
    public ResponseEntity<List<Loan>> getAllLoansByUserId(@PathVariable Long userId){
        List<Loan> loansList = loanService.getAllLoansByUserId(userId);

        if(loansList.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(loansList);
    }

    @PostMapping("/loans")
    public ResponseEntity<Loan> createLoan(@RequestParam Long userId, @RequestParam Long bookId){
            Loan loan = loanService.createLoan(userId, bookId);
            return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }


    @PutMapping("/loans/{loanId}/return")
    public ResponseEntity<Loan> returnLoan(@PathVariable Long loanId){
        Loan loan = loanService.returnLoan(loanId);
        return ResponseEntity.status(HttpStatus.OK).body(loan);
    }


    @PutMapping("/loans/{loanId}/extend")
    public ResponseEntity<Loan> extendLoan(@PathVariable Long loanId){
        Loan loan = loanService.extendLoan(loanId);
        return ResponseEntity.status(HttpStatus.OK).body(loan);
    }







}

package com.example.library_system.repository;

import com.example.library_system.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository <Loan, Long>  {
    List<Loan> findByUserId(Long userId);
    //Optional<Loan> findById(Long loanId);

}

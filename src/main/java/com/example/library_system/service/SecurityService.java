package com.example.library_system.service;

import com.example.library_system.entity.User;
import com.example.library_system.repository.LoanRepository;
import com.example.library_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanRepository loanRepository;

    //Både User och Admin
    public boolean hasAccessToLoan(Long userId, Authentication authentication) {
        // Hämta autentiserade användare
        String username = authentication.getName();
        Optional<User> optionalUser = userRepository.findByEmailContainingIgnoreCase(username);

        if (optionalUser.isPresent()) {
            User authenticatedUser = optionalUser.get();

            if (authenticatedUser.getUserId().equals(userId)) {
                return true;
            }
            // Eller om användaren är en admin
            return authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        }

        return false;
    }



}

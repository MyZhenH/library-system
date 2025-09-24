package com.example.library_system.repository;

import com.example.library_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {

    Optional<User> findByEmailContainingIgnoreCase(String email);
    Optional<User> findById(Long userId);





}

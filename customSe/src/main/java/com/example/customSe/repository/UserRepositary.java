package com.example.customSe.repository;

import com.example.customSe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface  UserRepositary extends JpaRepository<User, Long> {
    Optional<User> findByUsernameOrEmail(String username, String email); // Corrected
}

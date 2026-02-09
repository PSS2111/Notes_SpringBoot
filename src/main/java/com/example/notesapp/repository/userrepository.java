package com.example.notesapp.repository;

import com.example.notesapp.model.user;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface userrepository extends JpaRepository<user, Long> {
    Optional<user> findByEmail(String email);
    Optional<user> findByUsername(String username);
}
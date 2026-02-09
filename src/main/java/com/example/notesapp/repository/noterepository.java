package com.example.notesapp.repository;

import com.example.notesapp.model.note;
import com.example.notesapp.model.user;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface noterepository extends JpaRepository<note, Long> {
    // Custom query method: Spring writes the SQL for you!
    List<note> findByUserOrderByCreatedAtDesc(user user);
}
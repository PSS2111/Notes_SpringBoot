package com.example.notesapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users") // "user" is a reserved keyword in many databases
public class user {    // Class names should start with a Capital letter (User)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use this for auto-increment IDs
    private Long id;

    private String username;
    private String email;
    private String password;

    // Standard Getters and Setters are required unless you use Lombok
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
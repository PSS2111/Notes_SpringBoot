package com.example.notesapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.notesapp.model.note;
import com.example.notesapp.model.user;
import com.example.notesapp.repository.noterepository;
import com.example.notesapp.repository.userrepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/notes")
public class notecontroller {

    @Autowired
    private userrepository userRepository;
    @Autowired
    private noterepository noteRepository;
    @ModelAttribute
public void setResponseHeaders(HttpServletResponse response) {
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0
    response.setHeader("Expires", "0"); // Proxies
}

    @GetMapping("/")
public String showWelcomePage() {
    return "index";
}
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/signup")
    public String hello() {
        return "signup";
    }
    
    @PostMapping("/register")
    public String register(@ModelAttribute user user) {
        userRepository.save(user);
        return "redirect:/notes/login";
    }
    @PostMapping("/login")
public String login(@RequestParam String email, 
                    @RequestParam String password, 
                    HttpSession session, 
                    Model model) {

    Optional<user> foundUser = userRepository.findByEmail(email);
    if (foundUser.isPresent() && foundUser.get().getPassword().equals(password)) {
        
        session.setAttribute("loggedInUser", foundUser.get());
        return "redirect:/notes/dashboard";
    } else {
        model.addAttribute("loginError", "Invalid email or password");
        return "login"; 
    }
}
@GetMapping("/dashboard")
public String dashboard(HttpSession session, Model model) {
    user user = (user) session.getAttribute("loggedInUser");
    
    if (user == null) {
        return "redirect:/notes/login";
    }

    // Fetch only the notes for THIS user
    List<note> userNotes = noteRepository.findByUserOrderByCreatedAtDesc(user);
    model.addAttribute("notes", userNotes);
    
    return "dashboard";
}
@PostMapping("/add")
public String saveOrUpdateNote(@ModelAttribute note note, HttpSession session) {
    user loggedInUser = (user) session.getAttribute("loggedInUser");
    
    if (loggedInUser == null) {
        return "redirect:/notes/login";
    }

    // Link the note to the user
    note.setUser(loggedInUser);
    
    // If the note already has an ID, JPA will UPDATE it. 
    // If it has no ID, JPA will CREATE a new one.
    noteRepository.save(note);
    
    return "redirect:/notes/dashboard";
}

// @GetMapping("/edit/{id}")
// public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
//     user loggedInUser = (user) session.getAttribute("loggedInUser");
//     Optional<note> noteOpt = noteRepository.findById(id);

//     // Security check: only allow editing if the note exists and belongs to the user
//     if (noteOpt.isPresent() && noteOpt.get().getUser().getId().equals(loggedInUser.getId())) {
//         model.addAttribute("note", noteOpt.get());
//         return "edit-note"; // You will create this HTML file
//     }
    
//     return "redirect:/notes/dashboard";
// }

@GetMapping("/edit/{id}")
public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
    user loggedInUser = (user) session.getAttribute("loggedInUser");

    // FIX: Check if loggedInUser is null BEFORE calling .getId()
    if (loggedInUser == null) {
        return "redirect:/notes/login";
    }

    Optional<note> noteOpt = noteRepository.findById(id);
    if (noteOpt.isPresent() && noteOpt.get().getUser().getId().equals(loggedInUser.getId())) {
        model.addAttribute("note", noteOpt.get());
        return "edit-note";
    }
    
    return "redirect:/notes/dashboard";
}
@GetMapping("/delete/{id}")
public String deleteNote(@PathVariable Long id, HttpSession session) {
    user loggedInUser = (user) session.getAttribute("loggedInUser");
    Optional<note> noteOpt = noteRepository.findById(id);

    // Security: Only delete if note exists and belongs to the logged-in user
    if (noteOpt.isPresent() && noteOpt.get().getUser().getId().equals(loggedInUser.getId())) {
        noteRepository.deleteById(id);
    }
    
    return "redirect:/notes/dashboard";
}
@GetMapping("/logout")
public String logout(HttpSession session) {
    // 1. This clears all data (like 'loggedInUser') from the session
    session.invalidate(); 
    
    // 2. Redirect back to the login page with a success message
    return "redirect:/notes/login?logout";
}
    
}
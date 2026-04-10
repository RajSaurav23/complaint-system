package com.example.complaintsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import com.example.complaintsystem.model.User;
import com.example.complaintsystem.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ✅ REGISTER METHOD (IMPORTANT FIX)
    public String register(User user) {
        userRepository.save(user);
        return "User Registered Successfully";
    }

    // ✅ LOGIN METHOD
    public String login(String email, String password) {

        List<User> users = userRepository.findByEmail(email);

        if (users.isEmpty()) {
            return "User not found";
        }

        // ✅ FIXED LINE (removed unnecessary check)
        User u = users.get(0);

        if (u.getPassword().equals(password)) {
            return "Login Successful";
        } else {
            return "Invalid Password";
        }
    }
}
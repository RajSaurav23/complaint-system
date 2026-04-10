package com.example.complaintsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.example.complaintsystem.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findByEmail(String email);

}
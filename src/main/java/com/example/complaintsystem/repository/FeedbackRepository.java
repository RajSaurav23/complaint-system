package com.example.complaintsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.complaintsystem.model.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
}
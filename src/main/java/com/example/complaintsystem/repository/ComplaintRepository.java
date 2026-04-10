package com.example.complaintsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.complaintsystem.model.Complaint;
import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {

    // ✅ Get complaints by status
    List<Complaint> findByStatus(String status);

    // ✅ Count complaints by status (for dashboard)
    long countByStatus(String status);
}
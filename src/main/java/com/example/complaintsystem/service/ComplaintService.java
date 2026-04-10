package com.example.complaintsystem.service;

import com.example.complaintsystem.model.Complaint;
import com.example.complaintsystem.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository repo;

    // ===== ADD =====
    public String addComplaint(Complaint c) {
        c.setStatus("PENDING");
        c.setDate(LocalDate.now().toString());
        repo.save(c);
        return "Complaint Added ✅";
    }

    // ===== GET ALL =====
    public List<Complaint> getAllComplaints() {
        return repo.findAll();
    }

    // 🔥 FIXED (IMPORTANT)
    public List<Complaint> getAll() {
        return repo.findAll();
    }

    // ===== UPDATE STATUS =====
    public String updateStatus(int id, String status) {

        Complaint c = repo.findById(id).orElse(null);

        if (c == null) {
            return "Complaint Not Found ❌";
        }

        c.setStatus(status.trim().toUpperCase());
        repo.save(c);

        return "Status Updated ✅";
    }

    // ===== DELETE =====
    public String deleteComplaint(int id){

        Complaint c = repo.findById(id).orElse(null);

        if(c == null){
            return "Complaint Not Found ❌";
        }

        System.out.println("Status from DB: " + c.getStatus());

        String status = c.getStatus();

        if(status == null || !status.toUpperCase().contains("RESOLVED")){
            return "Only RESOLVED complaints can be deleted ❌";
        }

        repo.deleteById(id);

        return "Deleted Successfully ✅";
    }

    // ===== DASHBOARD =====
    public Map<String, Object> getDashboardData(){

        List<Complaint> list = repo.findAll();

        int pending = 0, progress = 0, resolved = 0;

        for(Complaint c : list){
            if("PENDING".equalsIgnoreCase(c.getStatus())) pending++;
            else if("IN_PROGRESS".equalsIgnoreCase(c.getStatus())) progress++;
            else if("RESOLVED".equalsIgnoreCase(c.getStatus())) resolved++;
        }

        Map<String,Object> map = new HashMap<>();
        map.put("total", list.size());
        map.put("pending", pending);
        map.put("inProgress", progress);
        map.put("resolved", resolved);
        map.put("allComplaints", list);

        return map;
    }

    // ✅ NEW METHOD (FOR EMAIL FEATURE)
    public Complaint getComplaintById(int id){
        return repo.findById(id).orElse(null);
    }
}
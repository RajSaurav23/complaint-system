package com.example.complaintsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.complaintsystem.model.Feedback;
import com.example.complaintsystem.repository.FeedbackRepository;

@RestController
@CrossOrigin(origins = "*")
public class FeedbackController {

    @Autowired
    private FeedbackRepository repo;

    // SAVE
    @PostMapping("/feedback")
public String save(@RequestBody Feedback f){

    if(f == null){
        return "Invalid Data ❌";
    }

    repo.save(f);
    return "Feedback Saved ✅";
}

    // GET ALL (ADMIN)
    @GetMapping("/feedback")
    public List<Feedback> getAll(){
        return repo.findAll();
    }
}
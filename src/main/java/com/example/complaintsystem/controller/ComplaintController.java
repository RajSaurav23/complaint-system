package com.example.complaintsystem.controller;

import com.example.complaintsystem.model.Complaint;
import com.example.complaintsystem.service.ComplaintService;
import com.example.complaintsystem.service.GeminiService;
import com.example.complaintsystem.service.GroqService;
import com.example.complaintsystem.service.EmailService; // ✅ ADDED

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(
    origins = "*",
    allowedHeaders = "*",
    methods = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE,
        RequestMethod.OPTIONS
    }
)
@RestController
@RequestMapping("/complaint")
public class ComplaintController {

    @Autowired
    private ComplaintService service;

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private GroqService groqService;

    // ✅ STEP 5: Autowire EmailService
    @Autowired
    private EmailService emailService;

    // GET ALL
    @GetMapping("/all")
    public List<Complaint> getAll() {
        return service.getAllComplaints();
    }

    // UPDATE
    @PutMapping("/update/{id}/{status}")
    public String updateStatus(@PathVariable int id, @PathVariable String status) {

        String result = service.updateStatus(id, status);

        // 🔥 2. RESOLVED MAIL
        if(status.equalsIgnoreCase("RESOLVED")){
            Complaint complaint = service.getComplaintById(id); // ⚠️ make sure this method exists

            if(complaint != null && complaint.getEmail() != null){
                emailService.sendMail(
                    complaint.getEmail(),
                    "Complaint Resolved",
                    "Your complaint has been resolved.\nThank you!"
                );
            }
        }

        return result;
    }

    // ADD COMPLAINT
    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public String addComplaint(
            @RequestParam String name,
            @RequestParam(required = false) String email,
            @RequestParam String issue,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile image
    ) {

        System.out.println("Name: " + name);
        System.out.println("Email: " + email);

        Complaint c = new Complaint();
        c.setName(name);
        c.setEmail(email);
        c.setIssue(issue);
        c.setDescription(description);

        String text = (description != null && !description.isEmpty()) 
                ? description 
                : issue;

        // GROQ CATEGORY + PRIORITY
        String aiResult = groqService.askAI(
            "Classify this complaint into Category (Electricity, Water, Road, Other) " +
            "and Priority (High, Normal). Only return keywords.\nComplaint: " + text
        );

        System.out.println("AI RESPONSE: " + aiResult);

        String result = aiResult.toLowerCase();

        if(result.contains("electricity"))
            c.setCategory("Electricity");
        else if(result.contains("water"))
            c.setCategory("Water");
        else if(result.contains("road"))
            c.setCategory("Road");
        else
            c.setCategory("Other");

        if(result.contains("high"))
            c.setPriority("High");
        else
            c.setPriority("Normal");

        // EXTRA PRIORITY CHECK
        String ai = groqService.askAI(
            "Check if this complaint is urgent. Answer only HIGH or NORMAL: " + text
        );

        if(ai != null && ai.toLowerCase().contains("high")){
            c.setPriority("HIGH");
        } else {
            c.setPriority("NORMAL");
        }

        // GEMINI BACKUP
        String prompt = "Classify this complaint: " + text;
        String aiResponse = geminiService.askAI(prompt);

        System.out.println("Gemini RESPONSE (backup): " + aiResponse);

        // ✅ SAVE COMPLAINT
        String saveResult = service.addComplaint(c);

        // 🔥 1. ADD MAIL
        if(email != null && !email.isEmpty()){
            emailService.sendMail(
                email,
                "Complaint Submitted",
                "Your complaint has been registered successfully.\nIssue: " + c.getIssue()
            );
        }

        return saveResult;
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable int id){
        return service.deleteComplaint(id);
    }
}
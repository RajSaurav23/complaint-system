package com.example.complaintsystem.controller;

import com.example.complaintsystem.model.Complaint;
import com.example.complaintsystem.service.ComplaintService;
import com.example.complaintsystem.service.GroqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private ComplaintService service;

    @Autowired
    private GroqService groqService;

    // ===== MAIN DASHBOARD =====
    @GetMapping
    public Map<String, Object> getDashboard() {
        return service.getDashboardData();
    }

    // =========================================
    // 🤖 AI INSIGHT (FINAL + FORMATTED)
    // =========================================
    @GetMapping("/ai-insight")
    public String getAIInsight() {

        List<Complaint> list = service.getAllComplaints();

        StringBuilder data = new StringBuilder();

        int count = 0;
        for(Complaint c : list){
            data.append("Issue: ").append(c.getIssue())
                .append(", Status: ").append(c.getStatus())
                .append(" | ");

            count++;
            if(count >= 5) break;
        }

        // ✅ CLEAN DATA
        String cleanData = data.toString().replace("\"", "");

        // ✅ PROMPT
        String prompt = "Analyze complaints and give short insights: " + cleanData;

        String ai = groqService.askAI(prompt);

        // ✅ SAFETY
        if(ai == null || ai.isEmpty()){
            return "No AI insights available right now.";
        }

        // 🔥 CLEAN + FORMAT RESPONSE
        ai = ai.replace("\\n", "\n");

        String formatted =
                "🤖 AI Insights\n\n" +
                ai +
                "\n\n📌 Suggestion:\nFocus on resolving pending complaints and prevent repeated issues.";

        return formatted;
    }
}
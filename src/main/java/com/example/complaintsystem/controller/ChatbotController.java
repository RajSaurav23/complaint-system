package com.example.complaintsystem.controller;

import com.example.complaintsystem.service.GroqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatbot")
@CrossOrigin
public class ChatbotController {

    @Autowired
    private GroqService groqService;

    @PostMapping("/ask")
    public String ask(@RequestBody String message) {

        String text = message.toLowerCase();

        // 🔥 STEP 1: FAST RULE-BASED RESPONSE (NO AI)
        if(text.contains("water") || text.contains("leak") || text.contains("pani")){
            return "This is a Water complaint 💧. Please submit your complaint in the portal. Our team will resolve it soon.";
        }

        if(text.contains("electric") || text.contains("light") || text.contains("bijli")){
            return "This is an Electricity complaint ⚡. Please submit it in the portal. Our team will resolve it soon.";
        }

        if(text.contains("road") || text.contains("traffic")){
            return "This is a Road complaint 🛣️. Please submit it in the portal.";
        }

        // 🔥 STEP 2: AI (fallback)
        String prompt =
        "You are a complaint system assistant.\n" +
        "User message: " + message + "\n" +
        "Give short, clear, professional answer.";

        String aiResponse = groqService.askAI(prompt);

        if(aiResponse == null || aiResponse.isEmpty()){
            return "Please submit your complaint in the system.";
        }

        return aiResponse;
    }
}
package com.example.complaintsystem.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public class GroqService {

    public String askAI(String prompt) {

        String API_KEY = System.getenv("GROQ_API_KEY");

        // 🔥 DEBUG (remove later)
        if (API_KEY == null || API_KEY.isEmpty()) {
            return "ERROR: API KEY NOT FOUND (set GROQ_API_KEY)";
        }

        String url = "https://api.groq.com/openai/v1/chat/completions";

        RestTemplate restTemplate = new RestTemplate();

        String cleanPrompt = prompt
                .replace("\"", "'")
                .replace("\n", " ")
                .replace("\r", " ");

        String body = String.format("""
        {
          "model": "llama-3.1-8b-instant",
          "messages": [
            {
              "role": "system",
              "content": "You are a smart complaint management assistant. Understand Hindi and English. Give short, clear answers."
            },
            {
              "role": "user",
              "content": "%s"
            }
          ]
        }
        """, cleanPrompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY); // 🔥 safer

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, entity, String.class);

            String json = response.getBody();

            String reply = "";
            if (json != null && json.contains("content")) {
                int start = json.indexOf("\"content\":\"") + 11;
                int end = json.indexOf("\"", start);
                reply = json.substring(start, end);
            }

            return reply;

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }
}
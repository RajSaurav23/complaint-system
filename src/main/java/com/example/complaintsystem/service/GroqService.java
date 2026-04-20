package com.example.complaintsystem.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public class GroqService {

    private final String API_KEY = "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";

   public String askAI(String prompt) {

    String url = "https://api.groq.com/openai/v1/chat/completions";

    RestTemplate restTemplate = new RestTemplate();
   String cleanPrompt = prompt
        .replace("\"", "'")     // fix quotes
        .replace("\n", " ")     // remove new lines
        .replace("\r", " ");    // remove carriage return

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
    headers.setBearerAuth(API_KEY);

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
        return e.getMessage();
    }
   }
}

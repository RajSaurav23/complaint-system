package com.example.complaintsystem.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

@Service
public class GeminiService {

    private final String API_KEY = " " + System.getenv("GEMINI_API_KEY");

    public String askAI(String prompt) {

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.0-pro:generateContent?key=" + API_KEY;

        RestTemplate restTemplate = new RestTemplate();

        String body = """
        {
          "contents": [{
            "parts":[{"text":"%s"}]
          }]
        }
        """.formatted(prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, entity, String.class);

            return response.getBody() != null ? response.getBody() : "No response";

        } catch (Exception e) {
            e.printStackTrace();
             return e.getMessage();
        }
    }
}
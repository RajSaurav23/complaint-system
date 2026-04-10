package com.example.complaintsystem.service;

import org.springframework.stereotype.Service;

@Service
public class ComplaintAIService {

    public String detectCategory(String text) {

        if(text == null) return "Other";

        text = text.toLowerCase();

        if (text.contains("light") || text.contains("electricity") || text.contains("bijli") || text.contains("current"))
    return "Electricity";

        else if (text.contains("water"))
            return "Water";

        else if (text.contains("road"))
            return "Road";

        else
            return "Other";
    }

    public String detectPriority(String text) {

        if(text == null) return "Normal";

        text = text.toLowerCase();

        if (text.contains("urgent") || text.contains("not working") || text.contains("2 days"))
            return "High";

        else
            return "Normal";
    }
}
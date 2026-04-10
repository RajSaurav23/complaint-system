package com.example.complaintsystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "complaints")   // ✅ Table name added
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 500)
    private String issue;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String date;

    // ✅ ADDED FIELDS
    private String category;
    private String priority;

    // ===== DEFAULT CONSTRUCTOR (REQUIRED) =====
    public Complaint() {}

    // ===== PARAMETERIZED CONSTRUCTOR =====
    public Complaint(String name, String email, String issue, String description, String status, String date) {
        this.name = name;
        this.email = email;
        this.issue = issue;
        this.description = description;
        this.status = status;
        this.date = date;
    }

    // ===== GETTERS & SETTERS =====

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getIssue() { return issue; }
    public void setIssue(String issue) { this.issue = issue; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    // ✅ GETTERS & SETTERS (ADDED)

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
}
package edu.ccrm.domain;

import java.time.LocalDateTime;

public abstract class Person {
    protected String id;
    protected String fullName;
    protected String email;
    protected LocalDateTime createdDate;
    
    public Person(String id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.createdDate = LocalDateTime.now();
    }
    
    // Abstract method for polymorphism
    public abstract String getRole();
    
    // Getters and setters
    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    
    @Override
    public String toString() {
        return String.format("%s [ID: %s, Name: %s, Email: %s]", 
            getRole(), id, fullName, email);
    }
}

package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.util.*;

public class Student extends Person {
    private String regNo;
    private StudentStatus status;
    private Set<String> enrolledCourses;
    private Map<String, Grade> courseGrades;
    private LocalDateTime lastUpdated;
    
    // Static nested class for GPA statistics
    public static class GPAStatistics {
        private final double averageGPA;
        private final int totalStudents;
        
        public GPAStatistics(double averageGPA, int totalStudents) {
            this.averageGPA = averageGPA;
            this.totalStudents = totalStudents;
        }
        
        public double getAverageGPA() { return averageGPA; }
        public int getTotalStudents() { return totalStudents; }
    }
    
    public Student(String id, String regNo, String fullName, String email) {
        super(id, fullName, email);
        this.regNo = regNo;
        this.status = StudentStatus.ACTIVE;
        this.enrolledCourses = new HashSet<>();
        this.courseGrades = new HashMap<>();
        this.lastUpdated = LocalDateTime.now();
    }
    
    @Override
    public String getRole() {
        return "Student";
    }
    
    public void enrollInCourse(String courseCode) {
        enrolledCourses.add(courseCode);
        lastUpdated = LocalDateTime.now();
    }
    
    public void unenrollFromCourse(String courseCode) {
        enrolledCourses.remove(courseCode);
        courseGrades.remove(courseCode);
        lastUpdated = LocalDateTime.now();
    }
    
    public void recordGrade(String courseCode, Grade grade) {
        if (enrolledCourses.contains(courseCode)) {
            courseGrades.put(courseCode, grade);
            lastUpdated = LocalDateTime.now();
        }
    }
    
    public double calculateGPA() {
        if (courseGrades.isEmpty()) return 0.0;
        
        return courseGrades.values().stream()
            .mapToDouble(Grade::getGradePoints)
            .average()
            .orElse(0.0);
    }
    
    // Getters and setters
    public String getRegNo() { return regNo; }
    public StudentStatus getStatus() { return status; }
    public Set<String> getEnrolledCourses() { return new HashSet<>(enrolledCourses); }
    public Map<String, Grade> getCourseGrades() { return new HashMap<>(courseGrades); }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    
    public void setStatus(StudentStatus status) { 
        this.status = status;
        this.lastUpdated = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return String.format("Student [ID: %s, RegNo: %s, Name: %s, Status: %s, GPA: %.2f]",
            id, regNo, fullName, status, calculateGPA());
    }
}

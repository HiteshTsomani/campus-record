package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.util.*;

public class Course {
    private final String code;
    private String title;
    private final int credits;
    private String instructor;
    private Semester semester;
    private String department;
    private CourseStatus status;
    private final Set<String> enrolledStudents;
    private final LocalDateTime createdDate;
    
    // Builder pattern implementation
    public static class Builder {
        private final String code;
        private final String title;
        private final int credits;
        private String instructor = "";
        private Semester semester = Semester.FALL;
        private String department = "";
        
        public Builder(String code, String title, int credits) {
            this.code = code;
            this.title = title;
            this.credits = credits;
        }
        
        public Builder instructor(String instructor) {
            this.instructor = instructor;
            return this;
        }
        
        public Builder semester(Semester semester) {
            this.semester = semester;
            return this;
        }
        
        public Builder department(String department) {
            this.department = department;
            return this;
        }
        
        public Course build() {
            return new Course(this);
        }
    }
    
    private Course(Builder builder) {
        this.code = builder.code;
        this.title = builder.title;
        this.credits = builder.credits;
        this.instructor = builder.instructor;
        this.semester = builder.semester;
        this.department = builder.department;
        this.status = CourseStatus.ACTIVE;
        this.enrolledStudents = new HashSet<>();
        this.createdDate = LocalDateTime.now();
    }
    
    public boolean enrollStudent(String studentId) {
        return enrolledStudents.add(studentId);
    }
    
    public boolean unenrollStudent(String studentId) {
        return enrolledStudents.remove(studentId);
    }
    
    // Getters and setters
    public String getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public String getInstructor() { return instructor; }
    public Semester getSemester() { return semester; }
    public String getDepartment() { return department; }
    public CourseStatus getStatus() { return status; }
    public Set<String> getEnrolledStudents() { return new HashSet<>(enrolledStudents); }
    public LocalDateTime getCreatedDate() { return createdDate; }
    
    public void setTitle(String title) { this.title = title; }
    public void setInstructor(String instructor) { this.instructor = instructor; }
    public void setSemester(Semester semester) { this.semester = semester; }
    public void setStatus(CourseStatus status) { this.status = status; }
    
    @Override
    public String toString() {
        return String.format("Course [Code: %s, Title: %s, Credits: %d, Instructor: %s, " +
            "Semester: %s, Enrolled: %d]",
            code, title, credits, instructor, semester, enrolledStudents.size());
    }
}

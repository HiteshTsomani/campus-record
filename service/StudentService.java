package edu.ccrm.service;

import edu.ccrm.domain.*;
import edu.ccrm.exception.*;
import edu.ccrm.config.AppConfig;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.Path;

public class StudentService implements Persistable<Student>, Searchable<Student> {
    private final Map<String, Student> students = new HashMap<>();
    private final AppConfig config = AppConfig.getInstance();
    
    // Inner class for transcript operations
    public class TranscriptService {
        public void printTranscript(String studentId) {
            Student student = students.get(studentId);
            if (student != null) {
                System.out.println("\n=== OFFICIAL TRANSCRIPT ===");
                System.out.println("Student: " + student.getFullName() + " (" + student.getRegNo() + ")");
                System.out.println("Student ID: " + student.getId());
                System.out.println("\nCourses and Grades:");
                
                student.getCourseGrades().forEach((courseCode, grade) -> {
                    System.out.printf("%-10s: %s (%.1f points)%n", 
                        courseCode, grade, grade.getGradePoints());
                });
                
                System.out.printf("\nCumulative GPA: %.2f%n", student.calculateGPA());
                System.out.println("Date Generated: " + java.time.LocalDateTime.now());
            }
        }
    }
    
    private final TranscriptService transcriptService = new TranscriptService();
    
    public Student addStudent(String id, String regNo, String fullName, String email) {
        if (students.containsKey(id)) {
            throw new IllegalArgumentException("Student with ID " + id + " already exists");
        }
        
        Student student = new Student(id, regNo, fullName, email);
        students.put(id, student);
        return student;
    }
    
    public void enrollStudentInCourse(String studentId, String courseCode, CourseService courseService) 
            throws DuplicateEnrollmentException, MaxCreditLimitExceededException {
        
        Student student = findById(studentId);
        Course course = courseService.findById(courseCode);
        
        if (student == null || course == null) {
            throw new IllegalArgumentException("Student or course not found");
        }
        
        // Check for duplicate enrollment
        if (student.getEnrolledCourses().contains(courseCode)) {
            throw new DuplicateEnrollmentException(
                "Student " + studentId + " is already enrolled in course " + courseCode);
        }
        
        // Check credit limit
        int currentCredits = student.getEnrolledCourses().stream()
            .mapToInt(code -> {
                Course c = courseService.findById(code);
                return c != null ? c.getCredits() : 0;
            })
            .sum();
        
        int newTotalCredits = currentCredits + course.getCredits();
        if (newTotalCredits > config.getMaxCreditsPerSemester()) {
            throw new MaxCreditLimitExceededException(
                "Enrollment would exceed maximum credit limit", 
                newTotalCredits, config.getMaxCreditsPerSemester());
        }
        
        student.enrollInCourse(courseCode);
        course.enrollStudent(studentId);
    }
    
    public void unenrollStudentFromCourse(String studentId, String courseCode, CourseService courseService) {
        Student student = findById(studentId);
        Course course = courseService.findById(courseCode);
        
        if (student != null && course != null) {
            student.unenrollFromCourse(courseCode);
            course.unenrollStudent(studentId);
        }
    }
    
    public void recordGrade(String studentId, String courseCode, Grade grade) {
        Student student = findById(studentId);
        if (student != null) {
            student.recordGrade(courseCode, grade);
        }
    }
    
    public Student.GPAStatistics calculateGPAStatistics() {
        List<Student> activeStudents = findBy(s -> s.getStatus() == StudentStatus.ACTIVE);
        
        double avgGPA = activeStudents.stream()
            .mapToDouble(Student::calculateGPA)
            .average()
            .orElse(0.0);
        
        return new Student.GPAStatistics(avgGPA, activeStudents.size());
    }
    
    public TranscriptService getTranscriptService() {
        return transcriptService;
    }
    
    // Interface implementations
    @Override
    public List<Student> findAll() {
        return new ArrayList<>(students.values());
    }
    
    @Override
    public List<Student> findBy(Predicate<Student> predicate) {
        return students.values().stream()
            .filter(predicate)
            .collect(Collectors.toList());
    }
    
    @Override
    public Student findById(String id) {
        return students.get(id);
    }
    
    @Override
    public void save(Student student) throws IOException {
        students.put(student.getId(), student);
    }
    
    @Override
    public Student load(String id) throws IOException {
        return students.get(id);
    }
    
    @Override
    public void delete(String id) throws IOException {
        students.remove(id);
    }
    
    public boolean updateStudent(String id, String fullName, String email) {
        Student student = students.get(id);
        if (student != null) {
            student.setFullName(fullName);
            student.setEmail(email);
            return true;
        }
        return false;
    }
    
    public boolean deactivateStudent(String id) {
        Student student = students.get(id);
        if (student != null) {
            student.setStatus(StudentStatus.INACTIVE);
            return true;
        }
        return false;
    }
}

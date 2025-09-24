package edu.ccrm.cli;

import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.*;
import edu.ccrm.exception.*;
import edu.ccrm.io.FileIOService;
import edu.ccrm.service.*;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public class CCRMApplication {
    private final Scanner scanner;
    private final StudentService studentService;
    private final CourseService courseService;
    private final FileIOService fileIOService;
    private final AppConfig config;
    private boolean running = true;
    
    // Anonymous inner class for application startup
    private final Runnable startupTask = new Runnable() {
        @Override
        public void run() {
            System.out.println("=== " + config.getAppName() + " v" + config.getVersion() + " ===");
            config.printSystemInfo();
            loadSampleData();
        }
    };
    
    public CCRMApplication() {
        this.scanner = new Scanner(System.in);
        this.studentService = new StudentService();
        this.courseService = new CourseService();
        this.fileIOService = new FileIOService(studentService, courseService);
        this.config = AppConfig.getInstance();
    }
    
    public void start() {
        startupTask.run();
        
        // Main application loop
        mainLoop: while (running) {
            displayMainMenu();
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                switch (choice) {
                    case 1:
                        handleStudentManagement();
                        break;
                    case 2:
                        handleCourseManagement();
                        break;
                    case 3:
                        handleEnrollmentManagement();
                        break;
                    case 4:
                        handleGradeManagement();
                        break;
                    case 5:
                        handleFileOperations();
                        break;
                    case 6:
                        handleReports();
                        break;
                    case 0:
                        running = false;
                        break mainLoop; // Labeled jump
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        continue; // Continue statement
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        
        System.out.println("Thank you for using " + config.getAppName() + "!");
        scanner.close();
    }
    
    private void loadSampleData() {
        try {
            fileIOService.createSampleData();
            fileIOService.importStudentsFromCSV("sample_students.csv");
            fileIOService.importCoursesFromCSV("sample_courses.csv");
            System.out.println("Sample data loaded successfully!\n");
        } catch (IOException e) {
            System.out.println("Note: Could not load sample data - " + e.getMessage());
        }
    }
    
    private void displayMainMenu() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("CAMPUS COURSE AND RECORDS MANAGER");
        System.out.println("=".repeat(40));
        System.out.println("1. Student Management");
        System.out.println("2. Course Management");
        System.out.println("3. Enrollment Management");
        System.out.println("4. Grade Management");
        System.out.println("5. File Operations");
        System.out.println("6. Reports");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }
    
    private void handleStudentManagement() {
        System.out.println("\n=== STUDENT MANAGEMENT ===");
        System.out.println("1. Add Student");
        System.out.println("2. List All Students");
        System.out.println("3. Update Student");
        System.out.println("4. Deactivate Student");
        System.out.println("5. Print Student Profile");
        System.out.println("6. Print Student Transcript");
        System.out.print("Enter choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1 -> addStudent();
            case 2 -> listAllStudents();
            case 3 -> updateStudent();
            case 4 -> deactivateStudent();
            case 5 -> printStudentProfile();
            case 6 -> printStudentTranscript();
            default -> System.out.println("Invalid choice.");
        }
    }
    
    private void addStudent() {
        try {
            System.out.print("Enter Student ID: ");
            String id = scanner.nextLine();
            
            System.out.print("Enter Registration Number: ");
            String regNo = scanner.nextLine();
            
            System.out.print("Enter Full Name: ");
            String fullName = scanner.nextLine();
            
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();
            
            Student student = studentService.addStudent(id, regNo, fullName, email);
            System.out.println("Student added successfully: " + student);
            
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void listAllStudents() {
        List<Student> students = studentService.findAll();
        
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        
        System.out.println("\n=== ALL STUDENTS ===");
        
        // Enhanced for loop
        for (Student student : students) {
            System.out.println(student);
        }
        
        System.out.println("Total students: " + students.size());
    }
    
    private void updateStudent() {
        System.out.print("Enter Student ID to update: ");
        String id = scanner.nextLine();
        
        Student student = studentService.findById(id);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        
        System.out.print("Enter new full name: ");
        String fullName = scanner.nextLine();
        
        System.out.print("Enter new email: ");
        String email = scanner.nextLine();
        
        if (studentService.updateStudent(id, fullName, email)) {
            System.out.println("Student updated successfully.");
        } else {
            System.out.println("Failed to update student.");
        }
    }
    
    private void deactivateStudent() {
        System.out.print("Enter Student ID to deactivate: ");
        String id = scanner.nextLine();
        
        if (studentService.deactivateStudent(id)) {
            System.out.println("Student deactivated successfully.");
        } else {
            System.out.println("Student not found.");
        }
    }
    
    private void printStudentProfile() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        
        Student student = studentService.findById(id);
        if (student != null) {
            System.out.println("\n=== STUDENT PROFILE ===");
            System.out.println(student);
            System.out.println("Enrolled Courses: " + student.getEnrolledCourses());
        } else {
            System.out.println("Student not found.");
        }
    }
    
    private void printStudentTranscript() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        studentService.getTranscriptService().printTranscript(id);
    }
    
    private void handleCourseManagement() {
        System.out.println("\n=== COURSE MANAGEMENT ===");
        System.out.println("1. Add Course");
        System.out.println("2. List All Courses");
        System.out.println("3. Update Course");
        System.out.println("4. Search Courses");
        System.out.println("5. Sort Courses by Code");
        System.out.print("Enter choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1 -> addCourse();
            case 2 -> listAllCourses();
            case 3 -> updateCourse();
            case 4 -> searchCourses();
            case 5 -> courseService.sortCoursesByCode();
            default -> System.out.println("Invalid choice.");
        }
    }
    
    private void addCourse() {
        try {
            System.out.print("Enter Course Code: ");
            String code = scanner.nextLine().toUpperCase();
            
            System.out.print("Enter Course Title: ");
            String title = scanner.nextLine();
            
            System.out.print("Enter Credits: ");
            int credits = scanner.nextInt();
            scanner.nextLine();
            
            System.out.print("Enter Instructor: ");
            String instructor = scanner.nextLine();
            
            System.out.print("Enter Semester (SPRING/SUMMER/FALL): ");
            String semesterStr = scanner.nextLine();
            Semester semester = Semester.valueOf(semesterStr.toUpperCase());
            
            System.out.print("Enter Department: ");
            String department = scanner.nextLine();
            
            Course course = courseService.addCourse(code, title, credits, instructor, semester, department);
            System.out.println("Course added successfully: " + course);
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void listAllCourses() {
        List<Course> courses = courseService.findAll();
        
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        
        System.out.println("\n=== ALL COURSES ===");
        
        // Traditional for loop
        for (int i = 0; i < courses.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, courses.get(i));
        }
    }
    
    private void updateCourse() {
        System.out.print("Enter Course Code to update: ");
        String code = scanner.nextLine().toUpperCase();
        
        System.out.print("Enter new title: ");
        String title = scanner.nextLine();
        
        System.out.print("Enter new instructor: ");
        String instructor = scanner.nextLine();
        
        if (courseService.updateCourse(code, title, instructor)) {
            System.out.println("Course updated successfully.");
        } else {
            System.out.println("Course not found.");
        }
    }
    
    private void searchCourses() {
        System.out.println("Search by:");
        System.out.println("1. Instructor");
        System.out.println("2. Department");
        System.out.println("3. Semester");
        System.out.print("Enter choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        List<Course> results = new ArrayList<>();
        
        switch (choice) {
            case 1 -> {
                System.out.print("Enter instructor name: ");
                String instructor = scanner.nextLine();
                results = courseService.findByInstructor(instructor);
            }
            case 2 -> {
                System.out.print("Enter department: ");
                String department = scanner.nextLine();
                results = courseService.findByDepartment(department);
            }
            case 3 -> {
                System.out.print("Enter semester (SPRING/SUMMER/FALL): ");
                String semesterStr = scanner.nextLine();
                Semester semester = Semester.valueOf(semesterStr.toUpperCase());
                results = courseService.findBySemester(semester);
            }
        }
        
        if (results.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            System.out.println("\n=== SEARCH RESULTS ===");
            results.forEach(System.out::println); // Lambda expression
        }
    }
    
    private void handleEnrollmentManagement() {
        System.out.println("\n=== ENROLLMENT MANAGEMENT ===");
        System.out.println("1. Enroll Student");
        System.out.println("2. Unenroll Student");
        System.out.print("Enter choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1 -> enrollStudent();
            case 2 -> unenrollStudent();
            default -> System.out.println("Invalid choice.");
        }
    }
    
    private void enrollStudent() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine().toUpperCase();
        
        try {
            studentService.enrollStudentInCourse(studentId, courseCode, courseService);
            System.out.println("Student enrolled successfully!");
        } catch (DuplicateEnrollmentException e) {
            System.err.println("Enrollment Error: " + e.getMessage());
        } catch (MaxCreditLimitExceededException e) {
            System.err.println("Credit Limit Error: " + e.getMessage());
            System.err.println("Attempted: " + e.getAttemptedCredits() + 
                             ", Max: " + e.getMaxAllowed());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void unenrollStudent() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine().toUpperCase();
        
        studentService.unenrollStudentFromCourse(studentId, courseCode, courseService);
        System.out.println("Student unenrolled successfully!");
    }
    
    private void handleGradeManagement() {
        System.out.println("\n=== GRADE MANAGEMENT ===");
        System.out.println("1. Record Grade");
        System.out.println("2. View Student Grades");
        System.out.print("Enter choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1 -> recordGrade();
            case 2 -> viewStudentGrades();
            default -> System.out.println("Invalid choice.");
        }
    }
    
    private void recordGrade() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine().toUpperCase();
        
        System.out.println("Available grades:");
        Grade[] grades = Grade.values();
        
        // While loop demonstration
        int i = 0;
        while (i < grades.length) {
            System.out.printf("%d. %s - %s (%.1f points)%n", 
                i + 1, grades[i], grades[i].getDescription(), grades[i].getGradePoints());
            i++;
        }
        
        System.out.print("Enter grade number: ");
        int gradeNum = scanner.nextInt();
        scanner.nextLine();
        
        if (gradeNum >= 1 && gradeNum <= grades.length) {
            Grade grade = grades[gradeNum - 1];
            studentService.recordGrade(studentId, courseCode, grade);
            System.out.println("Grade recorded successfully!");
        } else {
            System.out.println("Invalid grade selection.");
        }
    }
    
    private void viewStudentGrades() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        Student student = studentService.findById(studentId);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        
        Map<String, Grade> grades = student.getCourseGrades();
        if (grades.isEmpty()) {
            System.out.println("No grades recorded.");
        } else {
            System.out.println("\n=== STUDENT GRADES ===");
            grades.forEach((course, grade) -> {
                System.out.printf("%-10s: %s (%.1f points)%n", 
                    course, grade, grade.getGradePoints());
            });
            System.out.printf("GPA: %.2f%n", student.calculateGPA());
        }
    }
    
    private void handleFileOperations() {
        System.out.println("\n=== FILE OPERATIONS ===");
        System.out.println("1. Import Students from CSV");
        System.out.println("2. Import Courses from CSV");
        System.out.println("3. Export Students to CSV");
        System.out.println("4. Export Courses to CSV");
        System.out.println("5. Create Backup");
        System.out.print("Enter choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        try {
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter CSV filename: ");
                    String filename = scanner.nextLine();
                    fileIOService.importStudentsFromCSV(filename);
                }
                case 2 -> {
                    System.out.print("Enter CSV filename: ");
                    String filename = scanner.nextLine();
                    fileIOService.importCoursesFromCSV(filename);
                }
                case 3 -> {
                    System.out.print("Enter output filename: ");
                    String filename = scanner.nextLine();
                    fileIOService.exportStudentsToCSV(filename);
                }
                case 4 -> {
                    System.out.print("Enter output filename: ");
                    String filename = scanner.nextLine();
                    fileIOService.exportCoursesToCSV(filename);
                }
                case 5 -> fileIOService.createBackup();
                default -> System.out.println("Invalid choice.");
            }
        } catch (IOException e) {
            System.err.println("File operation error: " + e.getMessage());
        }
    }
    
    private void handleReports() {
        System.out.println("\n=== REPORTS ===");
        System.out.println("1. GPA Statistics");
        System.out.println("2. Course Count by Department");
        System.out.print("Enter choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1 -> showGPAStatistics();
            case 2 -> showCourseStatistics();
            default -> System.out.println("Invalid choice.");
        }
    }
    
    private void showGPAStatistics() {
        Student.GPAStatistics stats = studentService.calculateGPAStatistics();
        System.out.println("\n=== GPA STATISTICS ===");
        System.out.printf("Total Active Students: %d%n", stats.getTotalStudents());
        System.out.printf("Average GPA: %.2f%n", stats.getAverageGPA());
        
        // Stream API demonstration for GPA distribution
        List<Student> students = studentService.findAll();
        long excellentCount = students.stream()
            .filter(s -> s.calculateGPA() >= 3.7)
            .count();
        
        System.out.printf("Students with GPA >= 3.7: %d%n", excellentCount);
    }
    
    private void showCourseStatistics() {
        Map<String, Long> deptCounts = courseService.getCourseCountByDepartment();
        
        System.out.println("\n=== COURSE STATISTICS BY DEPARTMENT ===");
        deptCounts.forEach((dept, count) -> {
            System.out.printf("%-20s: %d courses%n", dept, count);
        });
    }
    
    public static void main(String[] args) {
        System.out.println("Starting Campus Course and Records Manager...");
        
        // Assertions demonstration (enable with -ea flag)
        assert AppConfig.getInstance() != null : "AppConfig should not be null";
        
        CCRMApplication app = new CCRMApplication();
        
        try {
            app.start();
        } catch (OutOfMemoryError error) {
            // Error vs Exception demonstration
            System.err.println("Critical error: Out of memory!");
        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
        } finally {
            System.out.println("Application shutdown complete.");
        }
    }
}

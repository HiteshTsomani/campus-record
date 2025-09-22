package edu.ccrm.io;

import edu.ccrm.domain.*;
import edu.ccrm.service.*;
import edu.ccrm.util.RecursiveUtils;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class FileIOService {
    private final Path dataDir = Paths.get("data");
    private final Path backupDir = Paths.get("backups");
    private final StudentService studentService;
    private final CourseService courseService;
    
    public FileIOService(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
        createDirectories();
    }
    
    private void createDirectories() {
        try {
            Files.createDirectories(dataDir);
            Files.createDirectories(backupDir);
        } catch (IOException e) {
            System.err.println("Error creating directories: " + e.getMessage());
        }
    }
    
    public void importStudentsFromCSV(String filename) throws IOException {
        Path filePath = dataDir.resolve(filename);
        
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        
        try (Stream<String> lines = Files.lines(filePath)) {
            lines.skip(1) // Skip header
                .forEach(line -> {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        try {
                            studentService.addStudent(
                                parts[0].trim(), 
                                parts[1].trim(), 
                                parts[2].trim(), 
                                parts[3].trim()
                            );
                        } catch (Exception e) {
                            System.err.println("Error importing student: " + line);
                        }
                    }
                });
        }
    }
    
    public void importCoursesFromCSV(String filename) throws IOException {
        Path filePath = dataDir.resolve(filename);
        
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        
        try (Stream<String> lines = Files.lines(filePath)) {
            lines.skip(1)
                .forEach(line -> {
                    String[] parts = line.split(",");
                    if (parts.length >= 6) {
                        try {
                            courseService.addCourse(
                                parts[0].trim(),
                                parts[1].trim(),
                                Integer.parseInt(parts[2].trim()),
                                parts[3].trim(),
                                Semester.valueOf(parts[4].trim().toUpperCase()),
                                parts[5].trim()
                            );
                        } catch (Exception e) {
                            System.err.println("Error importing course: " + line);
                        }
                    }
                });
        }
    }
    
    public void exportStudentsToCSV(String filename) throws IOException {
        Path filePath = dataDir.resolve(filename);
        
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(filePath))) {
            writer.println("ID,RegNo,FullName,Email,Status,GPA");
            
            studentService.findAll().forEach(student -> {
                writer.printf("%s,%s,%s,%s,%s,%.2f%n",
                    student.getId(),
                    student.getRegNo(),
                    student.getFullName(),
                    student.getEmail(),
                    student.getStatus(),
                    student.calculateGPA());
            });
        }
        
        System.out.println("Students exported to: " + filePath);
    }
    
    public void exportCoursesToCSV(String filename) throws IOException {
        Path filePath = dataDir.resolve(filename);
        
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(filePath))) {
            writer.println("Code,Title,Credits,Instructor,Semester,Department,Status");
            
            courseService.findAll().forEach(course -> {
                writer.printf("%s,%s,%d,%s,%s,%s,%s%n",
                    course.getCode(),
                    course.getTitle(),
                    course.getCredits(),
                    course.getInstructor(),
                    course.getSemester(),
                    course.getDepartment(),
                    course.getStatus());
            });
        }
        
        System.out.println("Courses exported to: " + filePath);
    }
    
    public void createBackup() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = now.format(formatter);
        Path backupFolder = backupDir.resolve("backup_" + timestamp);
        
        Files.createDirectories(backupFolder);
        
        // Export current data to backup folder
        exportStudentsToCSV(backupFolder.resolve("students.csv").toString());
        exportCoursesToCSV(backupFolder.resolve("courses.csv").toString());
        
        System.out.println("Backup created: " + backupFolder);
        
        // Use recursive utility to show backup size
        long backupSize = RecursiveUtils.calculateDirectorySize(backupFolder);
        System.out.println("Backup size: " + formatBytes(backupSize));
    }
    
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        else if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        else return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }
    
    public void createSampleData() throws IOException {
        // Create sample student data
        Path studentsFile = dataDir.resolve("sample_students.csv");
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(studentsFile))) {
            writer.println("ID,RegNo,FullName,Email");
            writer.println("S001,2023001,John Smith,john.smith@university.edu");
            writer.println("S002,2023002,Jane Doe,jane.doe@university.edu");
            writer.println("S003,2023003,Bob Johnson,bob.johnson@university.edu");
        }
        
        // Create sample course data
        Path coursesFile = dataDir.resolve("sample_courses.csv");
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(coursesFile))) {
            writer.println("Code,Title,Credits,Instructor,Semester,Department");
            writer.println("CS101,Introduction to Programming,3,Dr. Smith,FALL,Computer Science");
            writer.println("CS102,Data Structures,4,Dr. Johnson,FALL,Computer Science");
            writer.println("MATH201,Calculus I,4,Prof. Wilson,FALL,Mathematics");
        }
        
        System.out.println("Sample data files created.");
    }
}

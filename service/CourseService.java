package edu.ccrm.service;

import edu.ccrm.domain.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.Path;

public class CourseService implements Persistable<Course>, Searchable<Course> {
    private final Map<String, Course> courses = new HashMap<>();
    
    public Course addCourse(String code, String title, int credits, String instructor, 
                           Semester semester, String department) {
        if (courses.containsKey(code)) {
            throw new IllegalArgumentException("Course with code " + code + " already exists");
        }
        
        Course course = new Course.Builder(code, title, credits)
            .instructor(instructor)
            .semester(semester)
            .department(department)
            .build();
        
        courses.put(code, course);
        return course;
    }
    
    public List<Course> findByInstructor(String instructor) {
        return findBy(course -> course.getInstructor().equalsIgnoreCase(instructor));
    }
    
    public List<Course> findByDepartment(String department) {
        return findBy(course -> course.getDepartment().equalsIgnoreCase(department));
    }
    
    public List<Course> findBySemester(Semester semester) {
        return findBy(course -> course.getSemester() == semester);
    }
    
    // Stream API demonstration for GPA distribution
    public Map<String, Long> getCourseCountByDepartment() {
        return courses.values().stream()
            .collect(Collectors.groupingBy(
                Course::getDepartment,
                Collectors.counting()
            ));
    }
    
    // Array utilities demonstration
    public void sortCoursesByCode() {
        String[] courseCodes = courses.keySet().toArray(new String[0]);
        Arrays.sort(courseCodes); // Using Arrays class
        
        System.out.println("\n=== Courses Sorted by Code ===");
        for (String code : courseCodes) {
            System.out.println(courses.get(code));
        }
    }
    
    @Override
    public List<Course> findAll() {
        return new ArrayList<>(courses.values());
    }
    
    @Override
    public List<Course> findBy(Predicate<Course> predicate) {
        return courses.values().stream()
            .filter(predicate)
            .collect(Collectors.toList());
    }
    
    @Override
    public Course findById(String code) {
        return courses.get(code);
    }
    
    @Override
    public void save(Course course) throws IOException {
        courses.put(course.getCode(), course);
    }
    
    @Override
    public Course load(String code) throws IOException {
        return courses.get(code);
    }
    
    @Override
    public void delete(String code) throws IOException {
        courses.remove(code);
    }
    
    public boolean updateCourse(String code, String title, String instructor) {
        Course course = courses.get(code);
        if (course != null) {
            course.setTitle(title);
            course.setInstructor(instructor);
            return true;
        }
        return false;
    }
    
    public boolean deactivateCourse(String code) {
        Course course = courses.get(code);
        if (course != null) {
            course.setStatus(CourseStatus.INACTIVE);
            return true;
        }
        return false;
    }
}

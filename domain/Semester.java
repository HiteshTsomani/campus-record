package edu.ccrm.domain;

public enum Semester {
    SPRING(1, "Spring"),
    SUMMER(2, "Summer"), 
    FALL(3, "Fall");
    
    private final int value;
    private final String displayName;
    
    Semester(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }
    
    public int getValue() { return value; }
    public String getDisplayName() { return displayName; }
}

public enum Grade {
    S(4.0, "Excellent"),
    A(3.7, "Very Good"),
    B(3.0, "Good"),
    C(2.0, "Average"),
    F(0.0, "Fail");
    
    private final double gradePoints;
    private final String description;
    
    Grade(double gradePoints, String description) {
        this.gradePoints = gradePoints;
        this.description = description;
    }
    
    public double getGradePoints() { return gradePoints; }
    public String getDescription() { return description; }
}

public enum StudentStatus {
    ACTIVE, INACTIVE, GRADUATED, SUSPENDED
}

public enum CourseStatus {
    ACTIVE, INACTIVE, COMPLETED, CANCELLED
}

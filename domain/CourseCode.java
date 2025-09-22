package edu.ccrm.domain;

public final class CourseCode {
    private final String code;
    private final String department;
    
    public CourseCode(String fullCode) {
        // Parse code like "CS101" -> department="CS", code="101"
        if (fullCode == null || fullCode.length() < 3) {
            throw new IllegalArgumentException("Invalid course code");
        }
        
        int i = 0;
        while (i < fullCode.length() && Character.isLetter(fullCode.charAt(i))) {
            i++;
        }
        
        this.department = fullCode.substring(0, i);
        this.code = fullCode.substring(i);
    }
    
    public String getCode() { return code; }
    public String getDepartment() { return department; }
    public String getFullCode() { return department + code; }
    
    @Override
    public String toString() {
        return getFullCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CourseCode that = (CourseCode) obj;
        return department.equals(that.department) && code.equals(that.code);
    }
    
    @Override
    public int hashCode() {
        return department.hashCode() * 31 + code.hashCode();
    }
}

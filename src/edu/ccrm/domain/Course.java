package edu.ccrm.domain;
import java.util.Objects;

/**
 * Course with Builder pattern.
 */
public class Course {
    private final CourseCode code; // immutable component
    private String title;
    private int credits;
    private Instructor instructor;
    private Semester semester;
    private String department;
    private boolean active = true;

    private Course(Builder b) {
        this.code = new CourseCode(b.code);
        this.title = b.title;
        this.credits = b.credits;
        this.instructor = b.instructor;
        this.semester = b.semester;
        this.department = b.department;
    }

    public CourseCode getCourseCode() { return code; }
    public String getCode() { return code.getCode(); }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public Instructor getInstructor() { return instructor; }
    public Semester getSemester() { return semester; }
    public String getDepartment() { return department; }
    public boolean isActive() { return active; }

    public void setTitle(String title) { this.title = title; }
    public void setCredits(int credits) { this.credits = credits; }
    public void setInstructor(Instructor i) { this.instructor = i; }
    public void setSemester(Semester s) { this.semester = s; }
    public void setDepartment(String d) { this.department = d; }
    public void deactivate() { this.active = false; }

    @Override
    public String toString() {
        return String.format("Course[%s - %s (%d credits) dept=%s sem=%s inst=%s]",
                getCode(), title, credits, department, semester, instructor != null ? instructor.getFullName() : "<unassigned>");
    }

    // Builder (demonstrates Builder pattern)
    public static class Builder {
        private String code;
        private String title;
        private int credits;
        private Instructor instructor;
        private Semester semester;
        private String department;

        public Builder code(String code) { this.code = code; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder credits(int credits) { this.credits = credits; return this; }
        public Builder instructor(Instructor i) { this.instructor = i; return this; }
        public Builder semester(Semester s) { this.semester = s; return this; }
        public Builder department(String d) { this.department = d; return this; }

        public Course build() {
            Objects.requireNonNull(code, "code required");
            Objects.requireNonNull(title, "title required");
            if (credits <= 0) throw new IllegalArgumentException("credits must be > 0");
            return new Course(this);
 }
}
}

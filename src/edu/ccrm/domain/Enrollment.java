package edu.ccrm.domain;
import java.time.LocalDate;

public class Enrollment {
    private final Student student;
    private final Course course;
    private final LocalDate enrolledOn;
    private Integer marks; // 0-100
    private Grade grade;

    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.enrolledOn = LocalDate.now();
    }

    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public LocalDate getEnrolledOn() { return enrolledOn; }
    public Integer getMarks() { return marks; }
    public Grade getGrade() { return grade; }

    public void recordMarks(int marks) {
        if (marks < 0 || marks > 100) throw new IllegalArgumentException("Marks 0-100");
        this.marks = marks;
        this.grade = fromMarks(marks);
    }

    private Grade fromMarks(int m) {
        if (m >= 90) return Grade.S;
        if (m >= 80) return Grade.A;
        if (m >= 70) return Grade.B;
        if (m >= 60) return Grade.C;
        if (m >= 50) return Grade.D;
        if (m >= 40) return Grade.E;
        return Grade.F;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) : marks=%s grade=%s", course.getCode(), course.getTitle(), marks == null ? "-" : marks, grade == null ? "-" :grade);
}
}

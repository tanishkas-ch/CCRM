package edu.ccrm.domain;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Student class. Demonstrates encapsulation, nested classes (inner + static), and polymorphism.
 */
public class Student extends Person {
    public enum Status { ACTIVE, INACTIVE }

    private String regNo;
    private Status status;
    private final Map<Course, Enrollment> enrollments = new HashMap<>();
    private LocalDate dob;

    public Student(String id, String regNo, String fullName, String email) {
        super(id, fullName, email);
        this.regNo = regNo;
        this.status = Status.ACTIVE;
        this.dob = LocalDate.now().minusYears(18);
    }

    public String getRegNo() { return regNo; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Collection<Enrollment> getEnrollments() {
        return Collections.unmodifiableCollection(enrollments.values());
    }

    public void enroll(Enrollment e) {
        enrollments.put(e.getCourse(), e);
    }

    public void unenroll(Course c) {
        enrollments.remove(c);
    }

    public Optional<Enrollment> getEnrollmentFor(Course c) {
        return Optional.ofNullable(enrollments.get(c));
    }

    @Override
    public String role() { return "Student"; }

    @Override
    public String toString() {
        String courses = enrollments.keySet().stream()
            .map(Course::getCode)
            .sorted()
            .collect(Collectors.joining(", "));
        return String.format("Student[id=%s, reg=%s, name=%s, status=%s, courses=[%s]]",
                id, regNo, fullName, status, courses);
    }

    // static nested helper class: ProfilePrinter
    public static class ProfilePrinter {
        public static String printProfile(Student s) {
            return String.format("Profile:\nName: %s\nRegNo: %s\nEmail: %s\nStatus: %s\nCreated: %s",
                    s.fullName, s.regNo, s.email, s.status, s.createdAt);
        }
    }

    // inner class: Transcript builder using outer instance
    public class TranscriptBuilder {
        private final StringBuilder sb = new StringBuilder();

        public TranscriptBuilder addHeader() {
            sb.append("Transcript for ").append(fullName).append(" ("+regNo+")\n");
            return this;
        }

        public TranscriptBuilder addCourseLines() {
            for (Enrollment e : enrollments.values()) {
                sb.append(e.toString()).append("\n");
            }
            return this;
        }

        public TranscriptBuilder addGPA(double gpa) {
            sb.append(String.format("GPA: %.2f\n", gpa));
            return this;
        }

        public String build() { return sb.toString();}
}
}

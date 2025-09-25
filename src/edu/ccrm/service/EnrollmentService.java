package edu.ccrm.service;
import edu.ccrm.domain.*;
import edu.ccrm.exceptions.DuplicateEnrollmentException;
import edu.ccrm.exceptions.MaxCreditLimitExceededException;

import java.util.*;

/**
 * Enrollment rules: max credits per semester = 18.
 */
public class EnrollmentService {
    public static final int MAX_CREDITS_PER_SEM = 18;

    public void enroll(Student s, Course c) throws DuplicateEnrollmentException, MaxCreditLimitExceededException {
        if (s.getEnrollmentFor(c).isPresent()) throw new DuplicateEnrollmentException("Already enrolled");
        int current = s.getEnrollments().stream().mapToInt(e -> e.getCourse().getCredits()).sum();
        if (current + c.getCredits() > MAX_CREDITS_PER_SEM) throw new MaxCreditLimitExceededException("Credit limit exceeded");
        Enrollment e = new Enrollment(s, c);
        s.enroll(e);
    }

    public void unenroll(Student s, Course c) {
        s.unenroll(c);
    }

    public void recordMarks(Student s, Course c, int marks) {
        s.getEnrollmentFor(c).ifPresent(en -> en.recordMarks(marks));
    }

    public double computeGPA(Student s) {
        // weighted average by credits
        int totalCredits = s.getEnrollments().stream().mapToInt(e -> e.getCourse().getCredits()).sum();
        if (totalCredits == 0) return 0.0;
        double totalPoints = s.getEnrollments().stream()
                .filter(e -> e.getGrade() != null)
                .mapToDouble(e -> e.getGrade().getPoints() * e.getCourse().getCredits())
                .sum();
        return totalPoints / totalCredits;
}
}

package edu.ccrm.util;
import edu.ccrm.domain.Student;

import java.util.Comparator;

public class Comparators {
    public static Comparator<Student> byNameAsc = (a,b) -> a.getFullName().compareToIgnoreCase(b.getFullName());
}
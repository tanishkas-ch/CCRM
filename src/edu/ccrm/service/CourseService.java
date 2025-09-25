package edu.ccrm.service;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.Instructor;
import edu.ccrm.domain.Semester;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CourseService {
    private final Map<String, Course> byCode = new LinkedHashMap<>();
    private final AtomicInteger gen = new AtomicInteger(1);

    public Course create(Course.Builder builder) {
        Course c = builder.build();
        byCode.put(c.getCode(), c);
        return c;
    }

    public Optional<Course> findByCode(String code) { return Optional.ofNullable(byCode.get(code)); }
    public List<Course> listAll() { return new ArrayList<>(byCode.values()); }

    // filter/search using Streams
    public List<Course> filter(String instructorName, String dept, Semester sem) {
        return byCode.values().stream()
                .filter(c -> c.isActive())
                .filter(c -> instructorName == null || (c.getInstructor() != null && c.getInstructor().getFullName().equalsIgnoreCase(instructorName)))
                .filter(c -> dept == null || c.getDepartment().equalsIgnoreCase(dept))
                .filter(c -> sem == null || c.getSemester() == sem)
                .collect(Collectors.toList());
}
}

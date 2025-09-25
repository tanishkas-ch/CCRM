package edu.ccrm.service;
import edu.ccrm.domain.Student;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Simple in-memory StudentService (no persistence) demonstrating CRUD and streams.
 */
public class StudentService {
    private final Map<String, Student> byId = new LinkedHashMap<>();
    private final AtomicInteger idGen = new AtomicInteger(1000);

    public Student create(String regNo, String name, String email) {
        String id = "S" + idGen.getAndIncrement();
        Student s = new Student(id, regNo, name, email);
        byId.put(id, s);
        return s;
    }

    public Optional<Student> findById(String id) { return Optional.ofNullable(byId.get(id)); }

    public List<Student> listAll() { return new ArrayList<>(byId.values()); }

    public List<Student> searchByName(String q) {
        String ql = q.toLowerCase();
        return byId.values().stream().filter(s -> s.getFullName().toLowerCase().contains(ql)).collect(Collectors.toList());
    }

    public void deactivate(String id) {
        Student s = byId.get(id);
        if (s != null) s.setStatus(Student.Status.INACTIVE);
}
}

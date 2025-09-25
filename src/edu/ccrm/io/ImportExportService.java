package edu.ccrm.io;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.CourseCode;
import edu.ccrm.domain.Instructor;
import edu.ccrm.domain.Student;
import edu.ccrm.domain.Semester;
import edu.ccrm.service.CourseService;
import edu.ccrm.service.StudentService;
import edu.ccrm.config.AppConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple CSV-like import/export using NIO.2 and Streams API.
 */
public class ImportExportService {
    private final StudentService ss;
    private final CourseService cs;
    private final Path dataDir;

    public ImportExportService(StudentService ss, CourseService cs) {
        this.ss = ss;
        this.cs = cs;
        this.dataDir = AppConfig.getInstance().getDataFolder();
        try { Files.createDirectories(dataDir); } catch (IOException e) { throw new RuntimeException(e); }
    }

    public void exportStudents(String filename) throws IOException {
        Path f = dataDir.resolve(filename);
        List<String> lines = ss.listAll().stream()
                .map(s -> String.join(",",
                        s.getId(), s.getRegNo(), s.getFullName(), s.getEmail(), s.getStatus().name()))
                .collect(Collectors.toList());
        Files.write(f, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        System.out.println("Exported students to " + f.toAbsolutePath());
    }

    public void exportCourses(String filename) throws IOException {
        Path f = dataDir.resolve(filename);
        List<String> lines = cs.listAll().stream()
                .map(c -> String.join(",",
                        c.getCode(), c.getTitle(), String.valueOf(c.getCredits()), c.getDepartment(), c.getSemester().name()))
                .collect(Collectors.toList());
        Files.write(f, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        System.out.println("Exported courses to " + f.toAbsolutePath());
    }

    public void importStudents(Path file) throws IOException {
        Files.lines(file).forEach(line -> {
            try {
                String[] parts = line.split(",");
                ss.create(parts[1], parts[2], parts[3]);
            } catch (Exception ex) {
                System.err.println("Skipping bad line: " + line);
            }
        });
    }

    public void importCourses(Path file) throws IOException {
        Files.lines(file).forEach(line -> {
            try {
                String[] p = line.split(",");
                Course.Builder b = new Course.Builder().code(p[0]).title(p[1]).credits(Integer.parseInt(p[2])).department(p[3]).semester(Semester.valueOf(p[4]));
                cs.create(b);
            } catch (Exception ex) {
                System.err.println("Skipping bad line: " + line);
            }
   });
}
}

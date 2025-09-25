import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.*;
import edu.ccrm.exceptions.DuplicateEnrollmentException;
import edu.ccrm.exceptions.MaxCreditLimitExceededException;
import edu.ccrm.io.BackupService;
import edu.ccrm.io.ImportExportService;
import edu.ccrm.service.CourseService;
import edu.ccrm.service.EnrollmentService;
import edu.ccrm.service.StudentService;
import edu.ccrm.util.RecursionUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Console CLI. Demonstrates switch, loops, labeled break, lambdas, anonymous inner class, recursion usage.
 */
public class Main {
    private final AppConfig cfg = AppConfig.getInstance();
    private final StudentService ss = new StudentService();
    private final CourseService cs = new CourseService();
    private final EnrollmentService es = new EnrollmentService();
    private final ImportExportService io = new ImportExportService(ss, cs);
    private final BackupService backup = new BackupService();

    private final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        Main app = new Main();
        app.seedDemoData();
        app.loop();
    }

    private void seedDemoData() {
        Student s1 = ss.create("2025CS001","Alice Walker","alice@example.com");
        Student s2 = ss.create("2025CS002","Bob Ray","bob@example.com");
        Instructor inst = new Instructor("I100","Dr. Smith","smith@uni.edu","CS");
        Course c1 = new Course.Builder().code("CS101").title("Intro to CS").credits(4).department("CS").semester(Semester.FALL).instructor(inst).build();
        Course c2 = new Course.Builder().code("MA101").title("Calculus").credits(4).department("Math").semester(Semester.FALL).instructor(inst).build();
        cs.create(new Course.Builder().code("CS101").title("Intro to CS").credits(4).department("CS").semester(Semester.FALL).instructor(inst));
        cs.create(new Course.Builder().code("MA101").title("Calculus").credits(4).department("Math").semester(Semester.FALL).instructor(inst));
    }

    private void loop() {
        outer:
        while (true) {
            printMenu();
            String choice = in.nextLine().trim();
            switch (choice) {
                case "1" -> manageStudents();
                case "2" -> manageCourses();
                case "3" -> manageEnrollment();
                case "4" -> importExport();
                case "5" -> backupAndSize();
                case "6" -> reports();
                case "0" -> {
                    System.out.println("Exiting. Platform note: Java SE (this app) is the standard edition for desktop/console apps.");
                    break outer; // labeled break demonstrated
                }
                default -> System.out.println("Unknown option");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== CCRM Main Menu ===");
        System.out.println("1) Manage Students");
        System.out.println("2) Manage Courses");
        System.out.println("3) Enrollment & Grades");
        System.out.println("4) Import/Export Data");
        System.out.println("5) Backup & Show Backup Size (recursive)");
        System.out.println("6) Reports");
        System.out.println("0) Exit");
        System.out.print("Choose> ");
    }

    private void manageStudents() {
        while (true) {
            System.out.println("\n-- Students --");
            System.out.println("1) Add student");
            System.out.println("2) List students");
            System.out.println("3) Print profile & transcript");
            System.out.println("9) Back");
            System.out.print("Choose> ");
            String c = in.nextLine().trim();
            switch (c) {
                case "1":
                    System.out.print("regNo: "); String reg = in.nextLine();
                    System.out.print("name: "); String nm = in.nextLine();
                    System.out.print("email: "); String em = in.nextLine();
                    ss.create(reg, nm, em);
                    System.out.println("Added.");
                    break;
                case "2":
                    List<Student> list = ss.listAll();
                    list.sort(edu.ccrm.util.Comparators.byNameAsc);
                    list.forEach(System.out::println);
                    break;
                case "3":
                    System.out.print("student id: "); String id = in.nextLine();
                    ss.findById(id).ifPresentOrElse(s -> {
                        System.out.println(Student.ProfilePrinter.printProfile(s));
                        Student.TranscriptBuilder tb = s.new TranscriptBuilder();
                        double gpa = es.computeGPA(s);
                        System.out.println(tb.addHeader().addCourseLines().addGPA(gpa).build());
                    }, () -> System.out.println("Not found"));
                    break;
                case "9": return;
                default: System.out.println("Unknown");
            }
        }
    }

    private void manageCourses() {
        while (true) {
            System.out.println("\n-- Courses --");
            System.out.println("1) Add course");
            System.out.println("2) List courses");
            System.out.println("3) Search/Filter");
            System.out.println("9) Back");
            System.out.print("Choose> ");
            String c = in.nextLine().trim();
            switch (c) {
                case "1":
                    System.out.print("code: "); String code = in.nextLine();
                    System.out.print("title: "); String title = in.nextLine();
                    System.out.print("credits: "); int credits = Integer.parseInt(in.nextLine());
                    System.out.print("department: "); String dept = in.nextLine();
                    System.out.print("semester (SPRING/SUMMER/FALL): "); Semester sem = Semester.valueOf(in.nextLine().trim().toUpperCase());
                    Course cobj = new Course.Builder().code(code).title(title).credits(credits).department(dept).semester(sem).build();
                    cs.create(new Course.Builder().code(code).title(title).credits(credits).department(dept).semester(sem));
                    System.out.println("Added course.");
                    break;
                case "2":
                    cs.listAll().forEach(System.out::println);
                    break;
                case "3":
                    System.out.print("instructor (or blank): "); String inst = in.nextLine();
                    System.out.print("department (or blank): "); String deptf = in.nextLine();
                    System.out.print("semester (or blank): "); String sems = in.nextLine();
                    Semester semf = sems.isBlank() ? null : Semester.valueOf(sems.toUpperCase());
                    List<Course> res = cs.filter(inst.isBlank() ? null : inst, deptf.isBlank() ? null : deptf, semf);
                    res.forEach(System.out::println);
                    break;
                case "9": return;
                default: System.out.println("Unknown");
            }
        }
    }

    private void manageEnrollment() {
        while (true) {
            System.out.println("\n-- Enrollment --");
            System.out.println("1) Enroll student");
            System.out.println("2) Unenroll student");
            System.out.println("3) Record marks");
            System.out.println("9) Back");
            System.out.print("Choose> ");
            String c = in.nextLine().trim();
            try {
                switch (c) {
                    case "1":
                        System.out.print("studentId: "); String sid = in.nextLine();
                        System.out.print("courseCode: "); String code = in.nextLine();
                        Student s = ss.findById(sid).orElseThrow(() -> new RuntimeException("Student not found"));
                        Course co = cs.findByCode(code).orElseThrow(() -> new RuntimeException("Course not found"));
                        es.enroll(s, co);
                        System.out.println("Enrolled.");
                        break;
                    case "2":
                        System.out.print("studentId: "); String sid2 = in.nextLine();
                        System.out.print("courseCode: "); String code2 = in.nextLine();
                        ss.findById(sid2).ifPresent(st -> cs.findByCode(code2).ifPresent(co2 -> { es.unenroll(st, co2); System.out.println("Unenrolled"); }));
                        break;
                    case "3":
                        System.out.print("studentId: "); String sid3 = in.nextLine();
                        System.out.print("courseCode: "); String code3 = in.nextLine();
                        System.out.print("marks: "); int m = Integer.parseInt(in.nextLine());
                        ss.findById(sid3).ifPresent(st3 -> cs.findByCode(code3).ifPresent(co3 -> { es.recordMarks(st3, co3, m); System.out.println("Recorded"); }));
                        break;
                    case "9": return;
                    default: System.out.println("Unknown");
                }
            } catch (DuplicateEnrollmentException | MaxCreditLimitExceededException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    private void importExport() {
        while (true) {
            System.out.println("\n-- Import/Export --");
            System.out.println("1) Export students");
            System.out.println("2) Export courses");
            System.out.println("9) Back");
            System.out.print("Choose> ");
            String c = in.nextLine();
            try {
                switch (c) {
                    case "1": io.exportStudents("students.csv"); break;
                    case "2": io.exportCourses("courses.csv"); break;
                    case "9": return;
                    default: System.out.println("Unknown");
                }
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    private void backupAndSize() {
        try {
            Path p = backup.backup();
            System.out.println("Backup created at " + p.toAbsolutePath());
            long size = backup.computeSizeRecursive(p);
            System.out.println("Backup size (bytes): " + size);
            System.out.println("Recursive listing (depth 2):");
            RecursionUtil.listByDepth(p, 2);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void reports() {
        System.out.println("\n-- Reports --");
        // example: top students by GPA
        ss.listAll().stream()
                .sorted((a,b) -> Double.compare(es.computeGPA(b), es.computeGPA(a)))
                .limit(5)
                .forEach(s -> System.out.println(s.getFullName() + " GPA: " + String.format("%.2f", es.computeGPA(s))));

        // GPA distribution aggregated via Stream pipeline
        System.out.println("GPA distribution:");
        ss.listAll().stream().map(es::computeGPA).forEach(g -> System.out.println(String.format("%.2f", g)));
}
}

package edu.ccrm.util;
import java.io.IOException;
import java.nio.file.*;

public class RecursionUtil {
    // recursive directory listing by depth
    public static void listByDepth(Path p, int maxDepth) throws IOException {
        Files.walk(p, maxDepth).forEach(System.out::println);
}
}
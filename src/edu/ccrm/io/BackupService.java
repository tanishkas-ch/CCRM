package edu.ccrm.io;
import edu.ccrm.config.AppConfig;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

/**
 * Backup service that copies exported files to a timestamped folder and computes size recursively.
 */
public class BackupService {
    private final Path dataDir = AppConfig.getInstance().getDataFolder();

    public Path backup() throws IOException {
        String ts = AppConfig.getInstance().timestamp();
        Path dest = dataDir.resolveSibling("backup_" + ts);
        Files.createDirectories(dest);
        // copy all files from dataDir into dest
        try (Stream<Path> p = Files.walk(dataDir, 1)) {
            p.filter(path -> !path.equals(dataDir)).forEach(path -> {
                try {
                    Files.copy(path, dest.resolve(path.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) { throw new RuntimeException(e); }
            });
        }
        return dest;
    }

    public long computeSizeRecursive(Path root) throws IOException {
        final long[] total = {0};
        Files.walkFileTree(root, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                total[0] += attrs.size();
                return FileVisitResult.CONTINUE;
            }
        });
        return total[0];
}
}

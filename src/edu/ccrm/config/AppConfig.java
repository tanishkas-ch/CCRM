package edu.ccrm.config;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Singleton configuration for the app (demonstrates Singleton pattern).
 */
public final class AppConfig {
    private static final AppConfig INSTANCE = new AppConfig();

    private final Path dataFolder;
    private final DateTimeFormatter tsFmt;

    private AppConfig() {
        // default data folder: ./data
        this.dataFolder = Paths.get(System.getProperty("user.dir"), "data");
        this.tsFmt = DateTimeFormatter.ofPattern("uuuuMMdd_HHmmss_z");
    }

    public static AppConfig getInstance() {
        return INSTANCE;
    }

    public Path getDataFolder() {
        return dataFolder;
    }

    public String timestamp() {
        return ZonedDateTime.now().format(tsFmt);
}
}

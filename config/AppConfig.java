package edu.ccrm.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AppConfig {
    private static volatile AppConfig instance;
    
    private final String appName = "Campus Course Records Manager";
    private final String version = "1.0.0";
    private final int maxCreditsPerSemester = 18;
    private final Path dataDirectory = Paths.get("data");
    private final Path backupDirectory = Paths.get("backups");
    
    private AppConfig() {}
    
    public static AppConfig getInstance() {
        if (instance == null) {
            synchronized (AppConfig.class) {
                if (instance == null) {
                    instance = new AppConfig();
                }
            }
        }
        return instance;
    }
    
    public String getAppName() { return appName; }
    public String getVersion() { return version; }
    public int getMaxCreditsPerSemester() { return maxCreditsPerSemester; }
    public Path getDataDirectory() { return dataDirectory; }
    public Path getBackupDirectory() { return backupDirectory; }
    
    public void printSystemInfo() {
        System.out.println("=== Java Platform Information ===");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("OS: " + System.getProperty("os.name"));
        System.out.println("Java SE Platform - Full-featured desktop/server development");
        System.out.println("vs Java ME (Mobile/Embedded) vs Java EE (Enterprise/Web)");
    }
}

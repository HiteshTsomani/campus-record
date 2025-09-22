package edu.ccrm.util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicLong;

public class RecursiveUtils {
    
    // Recursive method to calculate directory size
    public static long calculateDirectorySize(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            return 0;
        }
        
        AtomicLong size = new AtomicLong(0);
        
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                size.addAndGet(attrs.size());
                return FileVisitResult.CONTINUE;
            }
        });
        
        return size.get();
    }
    
    // Recursive method to list files by depth
    public static void listFilesByDepth(Path directory, int maxDepth) throws IOException {
        if (!Files.exists(directory)) {
            System.out.println("Directory does not exist: " + directory);
            return;
        }
        
        Files.walk(directory, maxDepth)
            .forEach(path -> {
                int depth = path.getNameCount() - directory.getNameCount();
                String indent = "  ".repeat(depth);
                System.out.println(indent + path.getFileName());
            });
    }
}

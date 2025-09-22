package edu.ccrm.service;

import java.io.IOException;
import java.nio.file.Path;

public interface Persistable<T> {
    void save(T entity) throws IOException;
    T load(String id) throws IOException;
    void delete(String id) throws IOException;
    
    // Default method for diamond problem resolution
    default void exportToFile(Path filePath) throws IOException {
        System.out.println("Default export implementation");
    }
}

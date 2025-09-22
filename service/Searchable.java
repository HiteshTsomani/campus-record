package edu.ccrm.service;

import java.util.List;
import java.util.function.Predicate;

public interface Searchable<T> {
    List<T> findAll();
    List<T> findBy(Predicate<T> predicate);
    T findById(String id);
    
    // Default method
    default long count() {
        return findAll().size();
    }
}

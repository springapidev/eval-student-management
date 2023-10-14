package com.rajaul.studentmanagement.service;

import com.rajaul.studentmanagement.entity.Course;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
/**
 * @author Mohammad Rajaul Islam
 * @version 1.0.0
 * @since 1.0.0
 */
public interface CourseService {
    Map<String, Object> findAll(int page, int size,String direction);
    Map<String, Object> searchByName(String name, int page, int size,String direction);
    Optional<Course> findById(Long id);
    Course save(Course course);
    Optional<Course> update(Course course);
    Optional<Course> updatePartial(Course course);
    boolean delete(Long id);
    boolean existById(Long id);
    boolean existByName(String name);
    Set<Course> findAllByNameContainingIgnoreCase(String name);
}

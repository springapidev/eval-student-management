package com.rajaul.studentmanagement.service;

import com.rajaul.studentmanagement.dto.StudentDto;
import com.rajaul.studentmanagement.entity.Course;
import com.rajaul.studentmanagement.entity.Student;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
/**
 * @author Mohammad Rajaul Islam
 * @version 1.0.0
 * @since 1.0.0
 */
public interface StudentService {
    Map<String, Object> findAll(int page, int size,String direction);
    Map<String, Object> searchByName(String name, int page, int size, String direction);
    Optional<Student> findById(Long id);
    Optional<Student> findByPhone(String phone);
    Optional<Student> findByEmail(String email);
    Optional<Student> findByRoll(int roll);
    Student save(Student student);
    Optional<Student> update(Student student);
    Optional<Student> updatePartial(Student student);
    boolean delete(Long id);
    boolean existById(Long id);
    boolean existByPhone(String phone);
    boolean existByEmail(String email);
    boolean existByRoll(int roll);
    int assignCourses(StudentDto dto);
}

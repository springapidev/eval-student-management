package com.rajaul.studentmanagement.repository;

import com.rajaul.studentmanagement.entity.Course;
import com.rajaul.studentmanagement.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.Set;
/**
 * @author Mohammad Rajaul Islam
 * @version 1.0.0
 * @since 1.0.0
 */
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByRoll(int roll);

    Optional<Student> findByPhone(String phone);

    Optional<Student> findByEmail(String email);

    Optional<Student> findByRoll(int roll);

    Page<Student> findAllByNameIgnoreCaseContainingOrCoursesIn(String name, Set<Course> courses, Pageable pageable);
    Set<Student> findByCoursesIn(Set<Course> courses);


    @Modifying
    @Query("UPDATE Student s SET s.courses=?1 WHERE s.id=?2")
    int assignCourses(Set<Course> courses, Long id);

}

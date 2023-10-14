package com.rajaul.studentmanagement.repository;

import com.rajaul.studentmanagement.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.Set;
/**
 * @author Mohammad Rajaul Islam
 * @version 1.0.0
 * @since 1.0.0
 */
public interface CourseRepository extends JpaRepository<Course,Long> {
    boolean existsByName(String name);
    Optional<Course> findByName(String name);
    Page<Course> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
    Set<Course> findAllByNameContainingIgnoreCase(String name);
    @Transactional
    @Modifying
    @Query("UPDATE Course c SET c.name=?1, c.description=?2 WHERE c.id=?3")
    int update(String name,String description,Long id);
}

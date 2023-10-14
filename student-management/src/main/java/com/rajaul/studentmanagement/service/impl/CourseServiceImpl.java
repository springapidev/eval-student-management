package com.rajaul.studentmanagement.service.impl;

import com.rajaul.studentmanagement.entity.Course;
import com.rajaul.studentmanagement.repository.CourseRepository;
import com.rajaul.studentmanagement.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourseServiceImpl implements CourseService{
    private final CourseRepository repository;



    @Cacheable(value = "cacheStore",key = "'myCourseList'")
    @Override
    public Map<String, Object> findAll(int page, int size,String direction) {
        Pageable paging = PageRequest.of(page, size, Sort.by("name").ascending());
        if (direction != null && direction.equalsIgnoreCase("DESC")) {
            paging = PageRequest.of(page, size, Sort.by("name").descending());
        }
        Page<Course> coursePage = this.repository.findAll(paging);
        log.info("Course List = {} "+coursePage.getContent());
        return Map.of(
                "courses", coursePage.getContent(),
                "totalPages", coursePage.getTotalPages(),
                "totalItems", coursePage.getTotalElements(),
                "currentPage", coursePage.getNumber()
        );
    }

    @Override
    public Map<String, Object> searchByName(String name, int page, int size,String direction) {
        Pageable paging = PageRequest.of(page, size, Sort.by("name").ascending());
        if (direction != null && direction.equalsIgnoreCase("DESC")) {
            paging = PageRequest.of(page, size, Sort.by("name").descending());
        }
        Page<Course> coursePage = this.repository.findAllByNameContainingIgnoreCase(name,paging);
        return Map.of(
                "courses", coursePage.getContent(),
                "totalPages", coursePage.getTotalPages(),
                "totalItems", coursePage.getTotalElements(),
                "currentPage", coursePage.getNumber()
        );
    }

    @Override
    public Optional<Course> findById(Long id) {
        if(existById(id)){
            return this.repository.findById(id);
        }
        return Optional.empty();
    }

   @CacheEvict(value = "cacheStore",key = "'myCourseList'")
    @Override
    public Course save(Course course) {
        return repository.save(course);
    }
   @CacheEvict(value = "cacheStore",key = "'myCourseList'")
    @Override
    public Optional<Course> update(Course course) {
        return Optional.ofNullable(course.getId())
                .flatMap(repository::findById)
                .map(existingCourse -> updateCourseFields(existingCourse, course))
                .filter(this::isCourseValid)
                .map(repository::save);
    }

    @Override
    public Optional<Course> updatePartial(Course course) {
        if (isCourseValid(course)) {
            int updatedRows = repository.update(course.getName(), course.getDescription(), course.getId());

            if (updatedRows > 0) {
                // If rows were updated, the operation was successful, so return the updated course.
                return Optional.of(course);
            } else {
                // If no rows were updated, the course with the given ID was not found, so return an empty Optional.
                return Optional.empty();
            }
        } else {
            // If the course is not valid, return an empty Optional.
            return Optional.empty();
        }
    }
    @CacheEvict(value = "cacheStore",key = "'myCourseList'")
    @Override
    public boolean delete(Long id) {
        if(existById(id)){
            this.repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean existById(Long id) {
        return this.repository.existsById(id);
    }

    @Override
    public boolean existByName(String name) {
        return this.repository.existsByName(name);
    }

    @Override
    public Set<Course> findAllByNameContainingIgnoreCase(String name) {
        return name != null && !name.isEmpty() ?
                this.repository.findAllByNameContainingIgnoreCase(name) :
                Collections.emptySet();
    }

    private boolean isCourseValid(Course course) {
        // Implement validation logic here
        return course != null && course.getName() != null && !course.getName().isEmpty();
    }
    private Course updateCourseFields(Course existingCourse, Course updatedCourse) {
        if (!updatedCourse.getName().isEmpty()) {
            existingCourse.setName(updatedCourse.getName());
        }
        if(!updatedCourse.getDescription().isEmpty()){
            existingCourse.setDescription(updatedCourse.getDescription());
        }
        return existingCourse;
    }
}

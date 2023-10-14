package com.rajaul.studentmanagement.controller;

import com.rajaul.studentmanagement.entity.Course;
import com.rajaul.studentmanagement.exception.DeleteSuccessMessage;
import com.rajaul.studentmanagement.exception.ResourceNotFoundException;
import com.rajaul.studentmanagement.exception.UniqueConstraintViolationException;
import com.rajaul.studentmanagement.service.CourseService;
import com.rajaul.studentmanagement.utils.ApiConstants;
import com.rajaul.studentmanagement.utils.LogMessageConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
/**
 * @author Mohammad Rajaul Islam
 * @version 1.0.0
 * @since 1.0.0
 */
@Tag(name = "Course", description = "Course APIs")
@AllArgsConstructor
@RestController
@RequestMapping(value = ApiConstants.API_COURSE_URL)
public class CourseController {
    private final CourseService service;

    @Operation(summary = "Create a new Course", tags = {"courses", "post"})
        @PostMapping("add")
    public ResponseEntity<Course> add(@Validated @RequestBody Course course) {

        if (course.getName() == null) {
            throw new ResourceNotFoundException(LogMessageConstants.COURSE + LogMessageConstants.CAN_NOT_BE_EMPTY);
        } else if (service.existByName(course.getName())) {
            throw new UniqueConstraintViolationException(LogMessageConstants.COURSE + LogMessageConstants.CAN_NOT_BE_DUPLICATE);
        } else {
            return ResponseEntity.ok(this.service.save(course));
        }

    }
    @Operation(summary = "Update an existing Course", tags = {"courses", "put"})
       @PutMapping("update")
    public ResponseEntity<Optional<Course>> update(@Validated @RequestBody Course course) {

        if (course.getId() == null) {
            throw new ResourceNotFoundException(LogMessageConstants.COURSE_ID + LogMessageConstants.CAN_NOT_BE_EMPTY);
        } else if (!this.service.existById(course.getId())) {
            throw new ResourceNotFoundException(LogMessageConstants.NO_ID_FOUND);
        } else if (course.getName().isEmpty()) {
            throw new ResourceNotFoundException(LogMessageConstants.COURSE_NAME + LogMessageConstants.CAN_NOT_BE_EMPTY);
        } else if (service.existByName(course.getName())) {
            Optional<Course> existingCourse = service.findById(course.getId());
            if (existingCourse.isPresent() && !existingCourse.get().getName().equals(course.getName())) {
                throw new UniqueConstraintViolationException(LogMessageConstants.CAN_NOT_BE_DUPLICATE);
            }
        }
        return ResponseEntity.ok(this.service.update(course));

    }

    @PatchMapping("update-partially")
    public ResponseEntity<Optional<Course>> updatePartially(@Validated @RequestBody Course course) {
        try {
            if (course.getId() == null) {
                throw new ResourceNotFoundException(LogMessageConstants.COURSE_ID + LogMessageConstants.CAN_NOT_BE_EMPTY);
            } else if (!this.service.existById(course.getId())) {
                throw new ResourceNotFoundException(LogMessageConstants.NO_ID_FOUND);
            } else if (course.getName().isEmpty()) {
                throw new ResourceNotFoundException(LogMessageConstants.COURSE_NAME + LogMessageConstants.CAN_NOT_BE_EMPTY);
            } else if (service.existByName(course.getName())) {
                Optional<Course> existingCourse = service.findById(course.getId());
                if (existingCourse.isPresent() && !existingCourse.get().getName().equalsIgnoreCase(course.getName())) {
                    throw new UniqueConstraintViolationException(LogMessageConstants.CAN_NOT_BE_DUPLICATE);
                }
            }
            return ResponseEntity.ok(this.service.updatePartial(course));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @Operation(summary = "Get All Courses by By Pagination", tags = {"courses", "get","search"})
      @GetMapping("list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "direction", defaultValue = "ASC", required = false) String direction) {
        try {
            Map<String, Object> map = this.service.findAll(page - 1, size, direction);
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }
    @Operation(summary = "Get Courses by searching by Course name", tags = {"courses", "get","search"})
     @GetMapping("search")
    public ResponseEntity<Map<String, Object>> searchByName(
            @RequestParam(name = "name", defaultValue = "", required = false) String name,
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "direction", defaultValue = "ASC", required = false) String direction) {
        try {
            Map<String, Object> map = this.service.searchByName(name, page - 1, size, direction);
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @Operation(summary = "Find a Single Course By ID", tags = {"course", "get"})
    @GetMapping("find/{id}")
    public ResponseEntity<Object> findById(@PathVariable(value = "id") Long id) {
        if (!this.service.existById(id)) {
            throw new ResourceNotFoundException(LogMessageConstants.NO_COURSE_FOUND_WITH_ID + id);
        }
        return ResponseEntity.ok(this.service.findById(id));
    }
    @Operation(summary = "Delete Single Course By ID", tags = {"course", "delete"})
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") Long id) {
        if (!this.service.existById(id)) {
            throw new ResourceNotFoundException(LogMessageConstants.NO_COURSE_FOUND_WITH_ID + id);
        }
        if (service.delete(id)) {
            throw new DeleteSuccessMessage(LogMessageConstants.DELETED_SUCCESS);
        }
        return ResponseEntity.noContent().build();
    }


}






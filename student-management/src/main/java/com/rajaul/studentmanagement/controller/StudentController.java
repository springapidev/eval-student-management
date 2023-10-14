package com.rajaul.studentmanagement.controller;

import com.rajaul.studentmanagement.dto.StudentDto;
import com.rajaul.studentmanagement.entity.Student;
import com.rajaul.studentmanagement.exception.*;
import com.rajaul.studentmanagement.service.StudentService;
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
@Tag(name = "Student", description = "Student APIs")
@AllArgsConstructor
@RestController
@RequestMapping(value = ApiConstants.API_STUDENT_URL)
public class StudentController {
    private final StudentService service;

    @Operation(summary = "Create a new Course", tags = {"courses", "post"})
    @PostMapping("add")
    public ResponseEntity<Student> add(@Validated @RequestBody Student student) {

        if (student.getName() == null) {
            throw new ResourceNotFoundException(LogMessageConstants.STUDENT_NAME + LogMessageConstants.CAN_NOT_BE_EMPTY);
        } else if (student.getPhone() == null) {
            throw new ResourceNotFoundException(LogMessageConstants.STUDENT_PHONE + LogMessageConstants.CAN_NOT_BE_EMPTY);
        } else if (student.getEmail() == null) {
            throw new ResourceNotFoundException(LogMessageConstants.STUDENT_EMAIL + LogMessageConstants.CAN_NOT_BE_EMPTY);
        } else if (student.getRoll() == 0) {
            throw new ResourceNotFoundException(LogMessageConstants.STUDENT_ROLL + LogMessageConstants.CAN_NOT_BE_EMPTY);
        } else if (service.existByPhone(student.getPhone())) {
            throw new UniqueConstraintViolationException(LogMessageConstants.STUDENT_PHONE + LogMessageConstants.CAN_NOT_BE_DUPLICATE);
        } else if (service.existByEmail(student.getEmail())) {
            throw new UniqueConstraintViolationException(LogMessageConstants.STUDENT_EMAIL + LogMessageConstants.CAN_NOT_BE_DUPLICATE);
        } else if (service.existByRoll(student.getRoll())) {
            throw new UniqueConstraintViolationException(LogMessageConstants.STUDENT_ROLL + LogMessageConstants.CAN_NOT_BE_DUPLICATE);
        } else {
            return ResponseEntity.ok(this.service.save(student));
        }

    }

    @Operation(summary = "Update an existing Course", tags = {"courses", "put"})
    @PutMapping("update")
    public ResponseEntity<Optional<Student>> update(@Validated @RequestBody Student student) {

        if (student.getId() == null) {
            throw new ResourceNotFoundException(LogMessageConstants.STUDENT_ID + LogMessageConstants.CAN_NOT_BE_EMPTY);
        } else if (!this.service.existById(student.getId())) {
            throw new ResourceNotFoundException(LogMessageConstants.NO_ID_FOUND);
        } else if (service.existByPhone(student.getPhone())) {
            Optional<Student> existingStudent = service.findById(student.getId());
            if (existingStudent.isPresent() && !existingStudent.get().getPhone().equalsIgnoreCase(student.getPhone())) {
                throw new UniqueConstraintViolationException(LogMessageConstants.STUDENT_PHONE + LogMessageConstants.CAN_NOT_BE_DUPLICATE);
            }
        } else if (service.existByEmail(student.getEmail())) {
            Optional<Student> existingStudent = service.findById(student.getId());
            if (existingStudent.isPresent() && !existingStudent.get().getEmail().equalsIgnoreCase(student.getEmail())) {
                throw new UniqueConstraintViolationException(LogMessageConstants.STUDENT_EMAIL + LogMessageConstants.CAN_NOT_BE_DUPLICATE);
            }
        } else if (service.existByRoll(student.getRoll())) {
            Optional<Student> existingStudent = service.findById(student.getId());
            if (existingStudent.isPresent() && existingStudent.get().getRoll() != student.getRoll()) {
                throw new UniqueConstraintViolationException(LogMessageConstants.STUDENT_ROLL + LogMessageConstants.CAN_NOT_BE_DUPLICATE);
            }
        }
        return ResponseEntity.ok(this.service.update(student));
    }

    @Operation(summary = "Assign one or more courses into a student", tags = {"students", "post"})
    @PostMapping("assign-courses")
    public ResponseEntity<Student> assignCourses(@Validated @RequestBody StudentDto studentDto) {

        if (studentDto.getId() == null) {
            throw new ResourceNotFoundException(LogMessageConstants.STUDENT_ID + LogMessageConstants.CAN_NOT_BE_EMPTY);
        } else if (!this.service.existById(studentDto.getId())) {
            throw new ResourceNotFoundException(LogMessageConstants.NO_ID_FOUND);
        } else if (studentDto.getCourses().isEmpty()) {
            throw new ResourceNotFoundException(LogMessageConstants.COURSE + LogMessageConstants.CAN_NOT_BE_EMPTY);
        }
        int status = this.service.assignCourses(studentDto);
        if (status > 0) {
            throw new AssignCoursesSuccessMessage("Assign Success!");
        } else {
            throw new UpdateNotSuccessMessage("Assign Not Success!");
        }
    }

    @Operation(summary = "Retrieve all Students By Pagination, Filter and Sorting", tags = {"courses", "get", "filter"})
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

    @Operation(summary = "Filter all Students By student name or course name", tags = {"students", "get", "filter"})
    @GetMapping("search")
    public ResponseEntity<Map<String, Object>> searchByName(
            @RequestParam(name = "name", defaultValue = "test", required = false) String name,
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

    @Operation(summary = "Find a Single Student By ID", tags = {"student", "get"})
    @GetMapping("find/{id}")
    public ResponseEntity<Object> findById(@PathVariable(value = "id") Long id) {
        if (!this.service.existById(id)) {
            throw new ResourceNotFoundException(LogMessageConstants.NO_STUDENT_FOUND_WITH_ID + id);
        }
        return ResponseEntity.ok(this.service.findById(id));
    }

    @Operation(summary = "Delete Single Student By ID", tags = {"student", "delete"})
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") Long id) {
        if (!this.service.existById(id)) {
            throw new ResourceNotFoundException(LogMessageConstants.NO_STUDENT_FOUND_WITH_ID + id);
        }
        if (service.delete(id)) {
            throw new DeleteSuccessMessage(LogMessageConstants.DELETED_SUCCESS);
        }
        return ResponseEntity.noContent().build();
    }


}






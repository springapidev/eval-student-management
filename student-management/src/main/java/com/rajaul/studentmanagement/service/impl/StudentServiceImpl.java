package com.rajaul.studentmanagement.service.impl;

import com.rajaul.studentmanagement.dto.CourseDto;
import com.rajaul.studentmanagement.dto.StudentDto;
import com.rajaul.studentmanagement.entity.Course;
import com.rajaul.studentmanagement.entity.Student;
import com.rajaul.studentmanagement.repository.StudentRepository;
import com.rajaul.studentmanagement.service.CourseService;
import com.rajaul.studentmanagement.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository repository;
    private final CourseService courseService;


    @Cacheable(value = "cacheStudent",key = "'myStudentList'")
    @Override
    public Map<String, Object> findAll(int page, int size, String direction) {
        Pageable paging = PageRequest.of(page, size, Sort.by("name").ascending());
        if (direction != null && direction.equalsIgnoreCase("DESC")) {
            paging = PageRequest.of(page, size, Sort.by("name").descending());
        }
        Page<Student> categoryPage = this.repository.findAll(paging);

        return Map.of(
                "students", categoryPage.getContent(),
                "totalPages", categoryPage.getTotalPages(),
                "totalItems", categoryPage.getTotalElements(),
                "currentPage", categoryPage.getNumber()
        );
    }

    @Override
    public Map<String, Object> searchByName(String name, int page, int size, String direction) {
        Pageable paging = PageRequest.of(page, size, Sort.by("name").ascending());
        if (direction != null && direction.equalsIgnoreCase("DESC")) {
            paging = PageRequest.of(page, size, Sort.by("name").descending());
        }
         Page<Student> studentPage = this.repository.findAllByNameIgnoreCaseContainingOrCoursesIn(name,this.courseService.findAllByNameContainingIgnoreCase(name) , paging);
        return Map.of(
                "students", studentPage.getContent(),
                "totalPages", studentPage.getTotalPages(),
                "totalItems", studentPage.getTotalElements(),
                "currentPage", studentPage.getNumber()
        );
    }

    @Override
    public Optional<Student> findById(Long id) {
        return existById(id) ? repository.findById(id) : Optional.empty();
    }

    @Override
    public Optional<Student> findByPhone(String phone) {
        return existByPhone(phone) ? this.repository.findByPhone(phone) : Optional.empty();
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        return existByEmail(email) ? this.repository.findByEmail(email) : Optional.empty();
    }

    @Override
    public Optional<Student> findByRoll(int roll) {
        return existByRoll(roll) ? this.repository.findByRoll(roll) : Optional.empty();
    }
    @CacheEvict(value = "cacheStudent",key = "'myStudentList'")
    @Override
    public Student save(Student student) {
        return this.repository.save(student);
    }
    @CacheEvict(value = "cacheStudent",key = "'myStudentList'")
    @Override
    public Optional<Student> update(Student student) {
        return Optional.ofNullable(student.getId())
                .flatMap(repository::findById)
                .map(existingStudent -> updateStudentFields(existingStudent, student))
                .filter(this::isStudentValid)
                .map(repository::save);
    }

    @Override
    public Optional<Student> updatePartial(Student student) {
        return Optional.empty();
    }
    @CacheEvict(value = "cacheStudent",key = "'myStudentList'")
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
        if (id != 0) {
            return this.repository.existsById(id);
        }
        return false;
    }

    @Override
    public boolean existByPhone(String phone) {
        if (phone != null && !phone.isEmpty()) {
            return this.repository.existsByPhone(phone);
        }
        return false;
    }


    @Override
    public boolean existByEmail(String email) {
        if (email != null && !email.isEmpty()) {
            return this.repository.existsByEmail(email);
        }
        return false;
    }

    @Override
    public boolean existByRoll(int roll) {
        if (roll != 0) {
            return this.repository.existsByRoll(roll);
        }
        return false;
    }

    @Override
    public int assignCourses(StudentDto dto) {

        if (dto.getId() != null && !dto.getCourses().isEmpty()) {
            Optional<Student> studentOptional = this.repository.findById(dto.getId());
            if (studentOptional.isPresent()) {
                Set<Course> existedCourses = studentOptional.get().getCourses();
                existedCourses.addAll(toCourses(dto.getCourses()));
                studentOptional.get().setCourses(existedCourses);
            }
            return this.repository.save(studentOptional.get()).getRoll();
        }
        return 0;
    }

    private Set<Course> toCourses(Set<CourseDto> dtos) {
        return dtos.stream()
                .filter(dto -> this.courseService.existById(dto.getId()))
                .map(dto -> this.courseService.findById(dto.getId()).orElseGet(Course::new))
                .collect(Collectors.toSet());
    }

    private boolean isStudentValid(Student student) {
        // Implement validation logic here
        return student != null && student.getName() != null && !student.getName().isEmpty() && !student.getPhone().isEmpty() && !student.getEmail().isEmpty() && student.getRoll() != 0;
    }

    private Student updateStudentFields(Student existingStudent, Student updatedStudent) {
        if (!updatedStudent.getName().isEmpty()) {
            existingStudent.setName(updatedStudent.getName());
        }
        if (!updatedStudent.getPhone().isEmpty()) {
            existingStudent.setPhone(updatedStudent.getPhone());
        }
        if (!updatedStudent.getEmail().isEmpty()) {
            existingStudent.setEmail(updatedStudent.getEmail());
        }
        if (updatedStudent.getRoll() != 0) {
            existingStudent.setRoll(updatedStudent.getRoll());
        }
        if (!updatedStudent.getAddress().isEmpty()) {
            existingStudent.setAddress(updatedStudent.getAddress());
        }
        if (!updatedStudent.getCourses().isEmpty()) {
            existingStudent.setCourses(updatedStudent.getCourses());
        }
        return existingStudent;
    }
}

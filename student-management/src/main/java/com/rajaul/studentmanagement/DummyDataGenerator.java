package com.rajaul.studentmanagement;

import com.rajaul.studentmanagement.entity.Course;
import com.rajaul.studentmanagement.entity.Student;
import com.rajaul.studentmanagement.repository.CourseRepository;
import com.rajaul.studentmanagement.repository.StudentRepository;
import com.rajaul.studentmanagement.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class DummyDataGenerator implements CommandLineRunner {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public void run(String... args) throws Exception {
        Random random = new Random();
        log.info("Data Insertions Started");
        try{
            for (int i = 0; i <= 100; i++) {
                Course course = new Course();
                course.setName(generatedNames().get(i) + " - " + i);
                course.setDescription("Course Description " + i);
                this.courseRepository.save(course);
                Student student = new Student();
                student.setName(generatedStudentNames().get(i));
                int phone = 6000000 + i;
                student.setPhone("0168" + phone);
                student.setEmail("email" + i + "@gmail.com");
                student.setRoll(i + 1);
                student.setAddress("Bangladesh " + i);
                this.studentRepository.save(student);
                log.info("Data Insert " + i);
            }
        }catch (DataIntegrityViolationException e){

        }

        log.info("Data Insertions End");
    }

    private List<String> generatedNames() {
        List<String> courseNames = List.of("Java", "Python", "C Programming", "C-Sharp", "Ruby", "Spring Boot", "Spring MVC");

        // Repeat the original list to create a larger list
        List<String> repeatedCourseNames = Stream.generate(() -> courseNames)
                .limit(150 / courseNames.size() + 1)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        // Shuffle the list to randomize the order
        Collections.shuffle(repeatedCourseNames);

        // Take the first 100 names
        return repeatedCourseNames.subList(0, 150);
    }

    private List<String> generatedStudentNames() {
        List<String> studentNames = List.of("Reza", "Sami", "Hasan", "Sejuti", "Sayonti", "Milton", "Rashed", "Robin", "Rony");

        // Repeat the original list to create a larger list
        List<String> repeatedCourseNames = Stream.generate(() -> studentNames)
                .limit(200 / studentNames.size() + 1)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        // Shuffle the list to randomize the order
        Collections.shuffle(repeatedCourseNames);

        // Take the first 100 names
        return repeatedCourseNames.subList(0, 200);
    }

}

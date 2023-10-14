package com.rajaul.studentmanagement.dto;

import com.rajaul.studentmanagement.entity.Course;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author Mohammad Rajaul Islam
 * @version 1.0.0
 * @since 1.0.0
 */
@Setter
@Getter
public class StudentDto  {
    @NotNull(message = "ID can not be null")
   private Long id;
    @NotNull(message = "Course Set can not be empty")
    private Set<CourseDto> courses;

}



package com.rajaul.studentmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * @author Mohammad Rajaul Islam
 * @version 1.0.0
 * @since 1.0.0
 */
@ToString
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Course extends IdBasedEntity{

    @NotBlank(message = "Course Name can not be blank!")
    @Column(unique = true,nullable = false)
    private String name;
    @Lob
    private String description;

}
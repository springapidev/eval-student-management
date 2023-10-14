package com.rajaul.studentmanagement.entity;

import com.rajaul.studentmanagement.annonations.ValidPhone;
import com.rajaul.studentmanagement.annonations.MaskData;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
@Entity
public final class Student extends IdBasedEntity  {
    @NotBlank(message = "Name can not be blank!")
    @Column(nullable = false)
    private String name;

    @MaskData
    @ValidPhone
    @NotBlank(message = "Phone can not be blank!")
    @Column(unique = true,nullable = false)
    private String phone;

    @MaskData
    @NotBlank(message = "Email can not be blank!")
    @Email(message = "Enter a valid email!")
    @Column(unique = true,nullable = false)
    private String email;

    @NotNull(message = "Roll can not be Zero!")
    @Column(unique = true,nullable = false)
    private int roll;
    @Lob
    private String address;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "course_id",referencedColumnName = "id"),
            uniqueConstraints = @UniqueConstraint(columnNames = { "student_id", "course_id" })
    )
    private Set<Course> courses;

}



package com.abhi.programming_corner.dto;

import lombok.Data;

@Data
public class CourseDTO {
    private Long courseId;
    private String courseName;
    private String courseDuration;
    private String courseDescription;
    private InstructorDTO instructor;
}

package com.abhi.programming_corner.dto;

import lombok.Data;

@Data
public class InstructorDTO {
    private Long instructorId;
    private String firstName;
    private String lastName;
    private String summary;
    private UserDTO user;
}

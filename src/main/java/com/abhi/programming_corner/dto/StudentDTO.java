package com.abhi.programming_corner.dto;

import lombok.Data;

@Data
public class StudentDTO {
    private Long studentId;
    private String firstName;
    private String lastName;
    private String level;
    private UserDTO user;
}

package com.abhi.programming_corner.service.mapper;

import com.abhi.programming_corner.dto.InstructorDTO;
import com.abhi.programming_corner.model.Instructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class InstructorMapper {

    public InstructorDTO fromInstructor(Instructor instructor) {
        InstructorDTO instructorDTO = new InstructorDTO();
        BeanUtils.copyProperties(instructor, instructorDTO);
        return instructorDTO;
    }

    public Instructor fromInstructorDTO(InstructorDTO instructorDTO) {
        Instructor instructor = new Instructor();
        BeanUtils.copyProperties(instructorDTO, instructor);
        return instructor;
    }

}

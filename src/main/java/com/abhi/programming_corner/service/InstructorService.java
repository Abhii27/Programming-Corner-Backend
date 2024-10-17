package com.abhi.programming_corner.service;

import com.abhi.programming_corner.dto.InstructorDTO;
import com.abhi.programming_corner.model.Course;
import com.abhi.programming_corner.model.Instructor;
import com.abhi.programming_corner.model.User;
import com.abhi.programming_corner.repository.InstructorRepository;
import com.abhi.programming_corner.service.mapper.InstructorMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class InstructorService {
    private final InstructorRepository instructorRepository;
    private final InstructorMapper instructorMapper;
    private final UserService userService;
    private final CourseService courseService;

    public Instructor loadInstructorById(Long instructorId) {
        return instructorRepository.findById(instructorId)
                .orElseThrow(() -> new EntityNotFoundException("Instructor with ID" + instructorId + " not found"));
    }

    public Page<InstructorDTO> findInstructorsByName(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Instructor> instructorsPage = instructorRepository.findInstructorsByName(name, pageRequest);
        return new PageImpl<>(instructorsPage.getContent()
                .stream()
                .map(instructorMapper::fromInstructor)
                .collect(Collectors.toList()), pageRequest, instructorsPage.getTotalElements());
    }

    public InstructorDTO loadInstructorByEmail(String email) {
        return instructorMapper.fromInstructor(instructorRepository.findInstructorByEmail(email));
    }

    public InstructorDTO createInstructor(InstructorDTO instructorDTO) {
        User user = userService.createUser(instructorDTO.getUser().getEmail(), instructorDTO.getUser().getPassword());
        userService.assignRoleToUser(user.getEmail(), "Instructor");
        Instructor instructor = instructorMapper.fromInstructorDTO(instructorDTO);
        instructor.setUser(user);
        Instructor savedInstructor = instructorRepository.save(instructor);
        return instructorMapper.fromInstructor(savedInstructor);
    }


    public InstructorDTO updateInstructor(InstructorDTO instructorDTO) {
        Instructor loadedInstructor = loadInstructorById(instructorDTO.getInstructorId());
        Instructor instructor = instructorMapper.fromInstructorDTO(instructorDTO);
        instructor.setUser(loadedInstructor.getUser());
        instructor.setCourses(loadedInstructor.getCourses());
        Instructor updatedInstructor = instructorRepository.save(instructor);
        return instructorMapper.fromInstructor(updatedInstructor);
    }


    public List<InstructorDTO> fetchInstructors() {
        return instructorRepository.findAll()
                .stream()
                .map(instructorMapper::fromInstructor)
                .collect(Collectors.toList());
    }


    public void removeInstructor(Long instructorId) {
        Instructor instructor = loadInstructorById(instructorId);
        for (Course course : instructor.getCourses()) {
            courseService.removeCourse(course.getCourseId());
        }
        instructorRepository.deleteById(instructorId);
    }
}

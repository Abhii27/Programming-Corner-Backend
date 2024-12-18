package com.abhi.programming_corner.web;

import com.abhi.programming_corner.dto.CourseDTO;
import com.abhi.programming_corner.dto.InstructorDTO;
import com.abhi.programming_corner.model.User;
import com.abhi.programming_corner.service.CourseService;
import com.abhi.programming_corner.service.InstructorService;
import com.abhi.programming_corner.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("instructors")
@RequiredArgsConstructor
public class InstructorRestController {
    private final InstructorService instructorService;
    private final UserService userService;
    private final CourseService courseService;

    @GetMapping
    @PreAuthorize("hasAuthority('Admin')")
    public Page<InstructorDTO> searchInstructors(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                                 @RequestParam(name = "size", defaultValue = "5") int size) {
        return instructorService.findInstructorsByName(keyword, page, size);
    }

    @GetMapping("all")
    @PreAuthorize("hasAuthority('Admin')")
    public List<InstructorDTO> findAllInstructors() {
        return instructorService.fetchInstructors();
    }

    @GetMapping("find")
    @PreAuthorize("hasAuthority('Instructor')")
    public InstructorDTO loadInstructorByEmail(@RequestParam(name = "email", defaultValue = "") String email) {
        return instructorService.loadInstructorByEmail(email);
    }

    @GetMapping("/{instructorId}/courses")
    @PreAuthorize("hasAnyAuthority('Admin','Instructor')")
    public Page<CourseDTO> coursesByInstructorId(@PathVariable Long instructorId,
                                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                                 @RequestParam(name = "size", defaultValue = "5") int size) {
        return courseService.fetchCoursesForInstructor(instructorId, page, size);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Admin')")
    public InstructorDTO saveInstructor(@RequestBody InstructorDTO instructorDTO) {
        User user = userService.loadUserByEmail(instructorDTO.getUser().getEmail());
        if (user != null) throw new RuntimeException("Email Already Exist");
        return instructorService.createInstructor(instructorDTO);
    }

    @PutMapping("{instructorId}")
    @PreAuthorize("hasAuthority('Instructor')")
    public InstructorDTO updateInstructor(@RequestBody InstructorDTO instructorDTO, @PathVariable Long instructorId) {
        instructorDTO.setInstructorId(instructorId);
        return instructorService.updateInstructor(instructorDTO);
    }

    @DeleteMapping("{instructorId}")
    @PreAuthorize("hasAuthority('Admin')")
    public void deleteInstructor(@PathVariable Long instructorId) {
        instructorService.removeInstructor(instructorId);
    }

}

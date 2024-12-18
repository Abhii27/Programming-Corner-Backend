package com.abhi.programming_corner.web;

import com.abhi.programming_corner.dto.CourseDTO;
import com.abhi.programming_corner.dto.StudentDTO;
import com.abhi.programming_corner.model.User;
import com.abhi.programming_corner.service.CourseService;
import com.abhi.programming_corner.service.StudentService;
import com.abhi.programming_corner.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("students")
@RequiredArgsConstructor
public class StudentRestController {
    private final StudentService studentService;
    private final UserService userService;
    private final CourseService courseService;

    @GetMapping
    @PreAuthorize("hasAuthority('Admin')")
    public Page<StudentDTO> searchStudents(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                           @RequestParam(name = "page", defaultValue = "0") int page,
                                           @RequestParam(name = "size", defaultValue = "5") int size) {
        return studentService.loadStudentsByName(keyword, page, size);
    }

    @GetMapping("/{studentId}/other-courses")
    @PreAuthorize("hasAuthority('Student')")
    public Page<CourseDTO> nonSubscribedCoursesByStudentId(@PathVariable Long studentId,
                                                           @RequestParam(name = "page", defaultValue = "0") int page,
                                                           @RequestParam(name = "size", defaultValue = "5") int size) {
        return courseService.fetchNonEnrolledInCoursesForStudent(studentId, page, size);
    }

    @GetMapping("/find")
    @PreAuthorize("hasAuthority('Student')")
    public StudentDTO loadStudentByEmail(@RequestParam(name = "email", defaultValue = "") String email) {
        return studentService.loadStudentByEmail(email);
    }

    @GetMapping("/{studentId}/courses")
    @PreAuthorize("hasAuthority('Student')")
    public Page<CourseDTO> coursesByStudentId(@PathVariable Long studentId,
                                              @RequestParam(name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "size", defaultValue = "5") int size) {
        return courseService.fetchCoursesForStudent(studentId, page, size);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Admin')")
    public StudentDTO saveStudent(@RequestBody StudentDTO studentDTO) {
        User user = userService.loadUserByEmail(studentDTO.getUser().getEmail());
        if (user != null) throw new RuntimeException("Email Already Exist");
        return studentService.createStudent(studentDTO);
    }

    @PutMapping("/{studentId}")
    @PreAuthorize("hasAuthority('Student')")
    public StudentDTO updateStudent(@RequestBody StudentDTO studentDTO, @PathVariable Long studentId) {
        studentDTO.setStudentId(studentId);
        return studentService.updateStudent(studentDTO);
    }

    @DeleteMapping("/{studentId}")
    @PreAuthorize("hasAuthority('Admin')")
    public void deleteStudent(@PathVariable Long studentId) {
        studentService.removeStudent(studentId);
    }
}

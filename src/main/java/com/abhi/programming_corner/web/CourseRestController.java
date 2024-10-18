package com.abhi.programming_corner.web;

import com.abhi.programming_corner.dto.CourseDTO;
import com.abhi.programming_corner.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("courses")
@RequiredArgsConstructor
public class CourseRestController {
    private final CourseService courseService;

    @GetMapping
    @PreAuthorize("hasAuthority('Admin')")
    public Page<CourseDTO> searchCourse(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                        @RequestParam(name = "size", defaultValue = "5") int size) {
        return courseService.findCoursesByCourseName(keyword, page, size);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('Admin','Instructor')")
    public CourseDTO saveCourse(@RequestBody CourseDTO courseDTO) {
        return courseService.createCourse(courseDTO);
    }

    @PostMapping("{courseId}/enroll/students/{studentId}")
    @PreAuthorize("hasAuthority('Student')")
    public void enrollStudentInCourse(@PathVariable Long courseId, @PathVariable Long studentId) {
        courseService.assignStudentToCourse(courseId, studentId);
    }

    @PutMapping("{courseId}")
    @PreAuthorize("hasAnyAuthority('Admin','Instructor')")
    public CourseDTO updateCourse(@RequestBody CourseDTO courseDTO, @PathVariable Long courseId) {
        courseDTO.setCourseId(courseId);
        return courseService.updateCourse(courseDTO);
    }

    @DeleteMapping("{courseId}")
    @PreAuthorize("hasAuthority('Admin')")
    public void deleteCourse(@PathVariable Long courseId) {

        courseService.removeCourse(courseId);
    }
}

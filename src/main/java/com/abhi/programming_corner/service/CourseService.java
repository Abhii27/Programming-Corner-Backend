package com.abhi.programming_corner.service;

import com.abhi.programming_corner.dto.CourseDTO;
import com.abhi.programming_corner.model.Course;
import com.abhi.programming_corner.model.Instructor;
import com.abhi.programming_corner.model.Student;
import com.abhi.programming_corner.repository.CourseRepository;
import com.abhi.programming_corner.repository.InstructorRepository;
import com.abhi.programming_corner.repository.StudentRepository;
import com.abhi.programming_corner.service.mapper.CourseMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;

    public Course loadCourseById(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(() ->
                new EntityNotFoundException("Course with ID" + courseId + " Not Found!"));
    }

    public CourseDTO createCourse(CourseDTO courseDTO) {
        Course course = courseMapper.fromCourseDTO(courseDTO);
        Instructor instructor = instructorRepository.findById(courseDTO.getInstructor().getInstructorId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Instructor with ID" +
                                courseDTO.getInstructor().getInstructorId() + " Not Found"));
        course.setInstructor(instructor);
        Course saveCourse = courseRepository.save(course);
        return courseMapper.fromCourse(saveCourse);

    }

    public CourseDTO updateCourse(CourseDTO courseDTO) {
        Course loadedCourse = loadCourseById(courseDTO.getCourseId());
        Instructor instructor = instructorRepository.findById(courseDTO.getInstructor().getInstructorId())
                .orElseThrow(() -> new EntityNotFoundException("Instructor with ID "
                        + courseDTO.getInstructor().getInstructorId() + " Not Found"));
        Course course = courseMapper.fromCourseDTO(courseDTO);
        course.setInstructor(instructor);
        course.setStudents(loadedCourse.getStudents());
        Course updatedCourse = courseRepository.save(course);
        return courseMapper.fromCourse(updatedCourse);
    }

    public Page<CourseDTO> findCoursesByCourseName(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> coursesPage = courseRepository.findCoursesByCourseNameContains(keyword, pageRequest);
        return new PageImpl<>(coursesPage.getContent()
                .stream()
                .map(courseMapper::fromCourse)
                .collect(Collectors.toList()), pageRequest, coursesPage.getTotalElements());
    }
    
    public void assignStudentToCourse(Long courseId, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Student with ID " + studentId + " Not Found"));
        Course course = loadCourseById(courseId);
        course.assignStudentToCourse(student);
    }

    public Page<CourseDTO> fetchCoursesForStudent(Long studentId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> studentCoursesPage = courseRepository.getCoursesByStudentId(studentId, pageRequest);
        return new PageImpl<>(studentCoursesPage.getContent()
                .stream()
                .map(courseMapper::fromCourse)
                .collect(Collectors.toList()), pageRequest, studentCoursesPage.getTotalElements());
    }

    public Page<CourseDTO> fetchNonEnrolledInCoursesForStudent(Long studentId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> nonEnrolledInCoursesPage = courseRepository
                .getNonEnrolledInCoursesByStudentId(studentId, pageRequest);
        return new PageImpl<>(nonEnrolledInCoursesPage.getContent()
                .stream()
                .map(courseMapper::fromCourse).
                collect(Collectors.toList()), pageRequest, nonEnrolledInCoursesPage.getTotalElements());
    }

    public void removeCourse(Long courseId) {
        courseRepository.deleteById(courseId);
    }

    public Page<CourseDTO> fetchCoursesForInstructor(Long instructorId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> instructorCoursesPage = courseRepository.getCoursesByInstructorId(instructorId, pageRequest);
        return new PageImpl<>(instructorCoursesPage.getContent()
                .stream()
                .map(courseMapper::fromCourse)
                .collect(Collectors.toList()), pageRequest, instructorCoursesPage.getTotalElements());
    }

}

package com.devsenior.cdiaz.course_security.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.devsenior.cdiaz.course_security.model.dto.CourseDto;
import com.devsenior.cdiaz.course_security.service.CourseService;

@RestController
@RequestMapping("/api/cursos")
public class CourseController {
    // GET /api/cursos, GET /api/cursos/{id}, POST /api/cursos y DELETE /api/cursos/{id}.

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<CourseDto> getAll() {
        return courseService.getAllCourses();
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    @GetMapping("/{id}")
    public CourseDto getById(@PathVariable("id") Long id) {
        return courseService.getCourseById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public CourseDto create(@RequestBody CourseDto course) {
        return courseService.addCourse(course);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public CourseDto delete(@PathVariable("id") Long id) {
        return courseService.deleteCourse(id);
    }

}

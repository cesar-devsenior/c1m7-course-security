package com.devsenior.cdiaz.course_security.service;

import java.util.List;

import com.devsenior.cdiaz.course_security.model.dto.CourseDto;

public interface CourseService {
    List<CourseDto> getAllCourses();

    CourseDto getCourseById(Long id);

    CourseDto addCourse(CourseDto course);

    CourseDto deleteCourse(Long id);
}

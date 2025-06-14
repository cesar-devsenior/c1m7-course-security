package com.devsenior.cdiaz.course_security.mapper;

import org.springframework.stereotype.Component;

import com.devsenior.cdiaz.course_security.exception.CourseMappingException;
import com.devsenior.cdiaz.course_security.model.dto.CourseDto;
import com.devsenior.cdiaz.course_security.model.entity.Course;

@Component
public class CourseMapper {

    public CourseDto entityToDto(Course course) {
        if (course == null) {
            throw new CourseMappingException("La entidad no está asignada correctamente");
        }

        var courseDto = new CourseDto();
        courseDto.setId(course.getId());
        courseDto.setTitle(course.getTitle());
        courseDto.setDescription(course.getDescription());

        return courseDto;
    }

    public Course dtoToEntity(CourseDto courseDto) {
        if (courseDto == null) {
            throw new CourseMappingException("El DTO no está asignado correctamente");
        }

        var course = new Course();
        course.setId(courseDto.getId());
        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());

        return course;
    }
}

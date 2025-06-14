package com.devsenior.cdiaz.course_security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devsenior.cdiaz.course_security.model.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}

package com.devsenior.cdiaz.course_security.exception;

public class CourseNotFoundException extends RuntimeException {
    
    public CourseNotFoundException() {
        super("El curso solicitado no existe");
    }
}

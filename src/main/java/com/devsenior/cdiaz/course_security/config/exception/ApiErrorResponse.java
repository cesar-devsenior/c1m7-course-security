package com.devsenior.cdiaz.course_security.config.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ApiErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private Integer status; // Código de estado HTTP numérico (ej: 400, 404)
    private String error;   // La frase del estado HTTP (ej: "Bad Request", "Not Found")
    private String message; // Mensaje detallado del error para el cliente
    private String path;

    public ApiErrorResponse(HttpStatus status, String message, String path) {
        this.timestamp = LocalDateTime.now(); // Obtiene la fecha y hora actual
        this.status = status.value();         // Convierte el enum HttpStatus a su valor numérico
        this.error = status.getReasonPhrase(); // Obtiene la frase descriptiva del HttpStatus
        this.message = message;
        this.path = path;
    }
}

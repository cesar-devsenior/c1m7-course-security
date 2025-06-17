package com.devsenior.cdiaz.course_security.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.devsenior.cdiaz.course_security.model.dto.AuthenticationRequest;
import com.devsenior.cdiaz.course_security.model.dto.AuthenticationResponse;

import jakarta.validation.Valid;


@RestController
public class AuthenticateController {
    
    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@Valid @RequestBody AuthenticationRequest body) {
        //TODO: process POST request
        
        return null;
    }
    
}

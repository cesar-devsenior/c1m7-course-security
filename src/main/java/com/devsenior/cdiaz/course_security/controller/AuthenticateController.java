package com.devsenior.cdiaz.course_security.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.devsenior.cdiaz.course_security.model.dto.AuthenticationRequest;
import com.devsenior.cdiaz.course_security.model.dto.AuthenticationResponse;
import com.devsenior.cdiaz.course_security.service.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AuthenticateController {

    private final AuthenticationService authenticationService;
    
    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@Valid @RequestBody AuthenticationRequest body) {
        return authenticationService.login(body);
    }
    
}

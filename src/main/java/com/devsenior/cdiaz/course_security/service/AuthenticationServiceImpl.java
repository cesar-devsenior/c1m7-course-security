package com.devsenior.cdiaz.course_security.service;

import org.springframework.stereotype.Service;

import com.devsenior.cdiaz.course_security.model.dto.AuthenticationRequest;
import com.devsenior.cdiaz.course_security.model.dto.AuthenticationResponse;
import com.devsenior.cdiaz.course_security.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    
    private final UserRepository userRepository;

    @Override
    public AuthenticationResponse login(AuthenticationRequest credentials) {
        // TODO Auto-generated method stub
        return null;
    }
}

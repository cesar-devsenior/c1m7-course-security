package com.devsenior.cdiaz.course_security.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.devsenior.cdiaz.course_security.model.dto.AuthenticationRequest;
import com.devsenior.cdiaz.course_security.model.dto.AuthenticationResponse;
import com.devsenior.cdiaz.course_security.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    public AuthenticationResponse login(AuthenticationRequest credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));

        var userDetails = userDetailsService.loadUserByUsername(credentials.getUsername());

        var jwt = jwtUtil.generateToken(userDetails);

        return new AuthenticationResponse(jwt);
    }
}

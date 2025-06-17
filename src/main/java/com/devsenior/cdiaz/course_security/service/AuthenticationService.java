package com.devsenior.cdiaz.course_security.service;

import com.devsenior.cdiaz.course_security.model.dto.AuthenticationRequest;
import com.devsenior.cdiaz.course_security.model.dto.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse login(AuthenticationRequest credentials);
}

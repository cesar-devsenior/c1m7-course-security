package com.devsenior.cdiaz.course_security.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}

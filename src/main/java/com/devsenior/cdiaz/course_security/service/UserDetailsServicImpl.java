package com.devsenior.cdiaz.course_security.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.devsenior.cdiaz.course_security.repository.UserRepository;

@Service
public class UserDetailsServicImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServicImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No existe un usuario con el nombre: " + username));

        var authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role)) // Asumiendo que los roles son "ROLE_USER", "ROLE_ADMIN" etc.
                .toList();

        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}

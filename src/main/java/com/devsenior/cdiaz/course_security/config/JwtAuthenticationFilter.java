package com.devsenior.cdiaz.course_security.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.devsenior.cdiaz.course_security.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("La peticion no tiene token");
            filterChain.doFilter(request, response);
            return;
        }

        var token = authHeader.substring(7);
        log.info("El token es: {}", token);

        var username = jwtUtil.extractUsername(token);
        log.info("El username es: {}", username);

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (username != null && authentication == null) {
            log.info("Hay que cargar el usuario al contexto");
            // var userDetails = userDetailsService.loadUserByUsername(username);

            List<String> roles = jwtUtil.extractClaim(token, (claim) -> {
                return (List<String>) claim.get("roles");
            });

            log.info("Roles {}", roles);

            var userDetails = new User(username, "0",
                    roles.stream()
                            .map(e -> new SimpleGrantedAuthority(e))
                            .toList());

            if (jwtUtil.validateToken(token, userDetails)) {
                var authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}

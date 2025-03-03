package ru.malik.spring.SprinSecurity.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.filters.ExpiresFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.malik.spring.SprinSecurity.security.JWTUtil;
import ru.malik.spring.SprinSecurity.service.PersonDetailsService;

import java.io.IOException;
@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final PersonDetailsService personDetailsService;
    private final HttpServletResponse httpServletResponse;

    @Autowired
    public JWTFilter(JWTUtil jwtUtil, PersonDetailsService personDetailsService, HttpServletResponse httpServletResponse) {
        this.jwtUtil = jwtUtil;
        this.personDetailsService = personDetailsService;
        this.httpServletResponse = httpServletResponse;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String authHead = httpRequest.getHeader("Authorization");
        if (authHead != null && !authHead.isBlank() && authHead.startsWith("Bearer ")) {
            String jwt = authHead.substring(7);

            if (jwt.isBlank()) {
                httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            } else {
                try {
                    String username = jwtUtil.validateTokenAndRetrieveClaims(jwt);
                    UserDetails userDetails = personDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails ,userDetails.getPassword(), userDetails.getAuthorities());


                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (JWTVerificationException e) {
                    httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT");
                }
            }
        }

        filterChain.doFilter(httpRequest, httpResponse);
    }
}

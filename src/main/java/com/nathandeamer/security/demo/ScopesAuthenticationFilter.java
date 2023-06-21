package com.nathandeamer.security.demo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Converts the headers sent from an API G/W into authorities.
public class ScopesAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final var principalId = request.getHeader(SecurityConfig.X_PRINCIPAL_HEADER);
        final var xScopesHeader = request.getHeader(SecurityConfig.X_SCOPES_HEADER);

        if (principalId != null && xScopesHeader!= null && !principalId.isEmpty() && !xScopesHeader.isEmpty()) {
            ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
            List<String> scopes = Arrays.asList(xScopesHeader.split(","));
            scopes.forEach(scope -> authorities.add(new SimpleGrantedAuthority(scope)));
            ScopesAuthentication authentication = new ScopesAuthentication(principalId, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

}

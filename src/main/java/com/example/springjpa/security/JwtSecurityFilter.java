package com.example.springjpa.security;

import com.example.springjpa.user.CustomUserDetailsService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtSecurityFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;
    @Autowired
    CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userName = "";
        if(authHeader!= null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            userName= jwtService.extractUserName(token);
        }

        if(StringUtils.isNotBlank(userName) && SecurityContextHolder.getContext().getAuthentication() ==null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            if(jwtService.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities() );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }
        filterChain.doFilter(request, response);
    }
}

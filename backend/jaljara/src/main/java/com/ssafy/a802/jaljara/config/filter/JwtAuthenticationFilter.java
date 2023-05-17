package com.ssafy.a802.jaljara.config.filter;

import com.ssafy.a802.jaljara.exception.ExceptionFactory;
import com.ssafy.a802.jaljara.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            String validToken = resolveToken(request);

            if (validToken != null && JwtUtil.isValidToken(validToken)) {
                Authentication authentication = JwtUtil.getAuthentication(validToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch(Exception e) {
            throw ExceptionFactory.jwtAuthenticateFail();
        }
    }

    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }else return null;
    }

}


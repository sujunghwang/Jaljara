package com.ssafy.a802.jaljara.config.filter;

import com.ssafy.a802.jaljara.exception.CustomException;
import com.ssafy.a802.jaljara.exception.ExceptionFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain){
        try {
            filterChain.doFilter(request,response);
        }catch (Exception e){
            try {
                e.printStackTrace();
                response.sendError(ExceptionFactory.jwtAuthenticateFail().getStatus().value(), ExceptionFactory.jwtAuthenticateFail().getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
    }
}

package com.ssafy.a802.jaljara.common;

import com.ssafy.a802.jaljara.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<?> handleCustomException(CustomException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(e.getMessage())
                .timestamp(e.getTimestamp().toString())
                .build(), e.getStatus());
    }
}
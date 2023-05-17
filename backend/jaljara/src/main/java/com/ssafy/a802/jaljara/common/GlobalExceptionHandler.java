package com.ssafy.a802.jaljara.common;

import com.ssafy.a802.jaljara.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;
import java.time.Instant;

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

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleException(Exception e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message("글로벌 핸들러 : " + e.getMessage())
                        .timestamp(Timestamp.from(Instant.now()).toString())
                        .build(), HttpStatus.BAD_REQUEST
        );
    }
}
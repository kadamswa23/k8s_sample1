package com.example.springjpa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Date;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<ErrorResponseDto> dataNotFound(NoDataFoundException ex){
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                        .msg(ex.getMessage())
                        .dateTime(new Date().toInstant()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ErrorResponseDto> userNotFound(AuthenticationException ex){
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .msg(ex.getMessage())
                .dateTime(new Date().toInstant()).build();
        return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
    }

}

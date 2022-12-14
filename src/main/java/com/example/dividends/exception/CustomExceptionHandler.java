package com.example.dividends.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {


    // 1. 예외 발생 -> 2. 해당 예외 클래스 객체 생성 -> 3. 핸들러 처리
    // Abstraction 클래스 및 자식 클래스와 관련된 모든 에러 클래스를 처리하겠다.
    @ExceptionHandler(AbstractException.class)
    protected ResponseEntity<?> handleCustomException(AbstractException e) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .code(e.getStatusCode())
                                                   .message(e.getMessage())
                                                   .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.resolve(e.getStatusCode()));
    }
}

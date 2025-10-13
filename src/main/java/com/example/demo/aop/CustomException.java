package com.example.demo.aop;

import org.springframework.http.HttpStatus;

public class CustomException extends BaseException {

    public CustomException(HttpStatus httpStatus, ErrorCode errorCode, Object... args) {
        super(errorCode, httpStatus, args);
    }

    public CustomException(String message, HttpStatus httpStatus, ErrorCode errorCode) {
        super(message, errorCode, httpStatus);
    }
}

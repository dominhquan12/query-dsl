package com.example.demo.aop;

import org.springframework.http.HttpStatus;

public class CustomNotFoundException extends BaseException {

    public CustomNotFoundException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.NOT_FOUND);
    }
}

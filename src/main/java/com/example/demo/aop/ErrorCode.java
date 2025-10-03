package com.example.demo.aop;

import lombok.Getter;

@Getter
public enum ErrorCode {
    BOOK_NOT_FOUND("001", "book not found");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

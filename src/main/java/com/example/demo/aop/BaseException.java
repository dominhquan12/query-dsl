package com.example.demo.aop;

import lombok.Data;
import org.springframework.http.HttpStatus;
@Data
public abstract class BaseException extends RuntimeException {
    protected ErrorCode errorCode;
    protected HttpStatus httpStatus;
    Object[] args;
    public BaseException(ErrorCode errorCode, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public BaseException(ErrorCode errorCode, HttpStatus httpStatus, Object... args) {
        super(formatMessage(errorCode, args));
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.args = args;
    }

    private static String formatMessage(ErrorCode errorCode, Object... args) {
        return args != null && args.length > 0 ? String.format(errorCode.getMessage(), args) : errorCode.getMessage();
    }

    public BaseException(String message, ErrorCode errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public BaseException(String message, Throwable cause, ErrorCode errorCode, HttpStatus httpStatus) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public BaseException(Throwable cause, ErrorCode errorCode, HttpStatus httpStatus) {
        super(cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode, HttpStatus httpStatus) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}

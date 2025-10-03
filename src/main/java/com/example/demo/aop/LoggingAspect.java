package com.example.demo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // --- Pointcut for Controller ---
    @Pointcut("execution(* com.example.demo..controller..*(..))")
    public void controllerMethods() {}

    // --- Pointcut for Service ---
    @Pointcut("execution(* com.example.demo..service..*(..))")
    public void serviceMethods() {}

    // --- Logging for Controller ---
    @Before("controllerMethods()")
    public void logBeforeController(JoinPoint joinPoint) {
        log.info("[Controller-Before] {} | args={}",
                joinPoint.getSignature().toShortString(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterReturningController(JoinPoint joinPoint, Object result) {
        log.info("[Controller-AfterReturning] {} | result={}",
                joinPoint.getSignature().toShortString(), result);
    }

    @AfterThrowing(pointcut = "controllerMethods()", throwing = "error")
    public void logAfterThrowingController(JoinPoint joinPoint, Throwable error) {
        log.error("[Controller-Exception] {} | error={}",
                joinPoint.getSignature().toShortString(), error.getMessage(), error);
    }

    // --- Logging for Service ---
    @Before("serviceMethods()")
    public void logBeforeService(JoinPoint joinPoint) {
        log.debug("[Service-Before] {} | args={}",
                joinPoint.getSignature().toShortString(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfterReturningService(JoinPoint joinPoint, Object result) {
        log.debug("[Service-AfterReturning] {} | result={}",
                joinPoint.getSignature().toShortString(), result);
    }

    @AfterThrowing(pointcut = "serviceMethods()", throwing = "error")
    public void logAfterThrowingService(JoinPoint joinPoint, Throwable error) {
        log.error("[Service-Exception] {} | error={}",
                joinPoint.getSignature().toShortString(), error.getMessage(), error);
    }

}

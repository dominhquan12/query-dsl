package com.example.demo.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // --- Pointcut for Controller ---
    @Pointcut("execution(* com.example.demo..controller..*(..))")
    public void controllerMethods() {
    }

    // --- Pointcut for Service ---
    @Pointcut("execution(* com.example.demo..service..*(..))")
    public void serviceMethods() {
    }

    // --- Logging for Controller ---
    @Before("controllerMethods()")
    public void logBeforeController(JoinPoint joinPoint) {
        // Get current HTTP request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            HttpServletRequest request = servletRequestAttributes.getRequest();

            // Collect request parameters (only those actually sent)
            Map<String, String[]> paramMap = request.getParameterMap();
            String params = paramMap.isEmpty()
                    ? "no parameters"
                    : paramMap
                    .entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + "=" + Arrays.toString(entry.getValue()))
                    .collect(Collectors.joining(", "));

            log.info("➡️ Received request: {} {} with {}", request.getMethod(), request.getRequestURI(), params);
        }

        // Get method signature and parameter names
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        // Build detailed argument log (including null values)
        String argString = IntStream.range(0, paramNames.length)
                .mapToObj(i -> paramNames[i] + "=" + (args[i] == null ? "null" : args[i].toString()))
                .collect(Collectors.joining(", "));

        log.info("[Custom-OpenAPI-Before] {} | args=[{}]", methodSignature.toShortString(), argString);
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterReturningController(JoinPoint joinPoint, Object result) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            log.info("✅ Completed request: {} {}", request.getMethod(), request.getRequestURI());
        }

        log.info(
                "[Custom-OpenAPI-AfterReturning] {} | result={}",
                joinPoint.getSignature().toShortString(),
                result != null ? result.toString() : "null"
        );
    }

    @AfterThrowing(pointcut = "controllerMethods()", throwing = "error")
    public void logAfterThrowingController(JoinPoint joinPoint, Throwable error) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            log.error("❌ Exception in request: {} {} | error={}", request.getMethod(), request.getRequestURI(), error.getMessage(), error);
        } else {
            log.error("❌ Exception in {} | error={}", joinPoint.getSignature().toShortString(), error.getMessage(), error);
        }
    }

//    // --- Logging for Service ---
//    @Before("serviceMethods()")
//    public void logBeforeService(JoinPoint joinPoint) {
//        log.debug("[Service-Before] {} | args={}",
//                joinPoint.getSignature().toShortString(),
//                Arrays.toString(joinPoint.getArgs()));
//    }
//
//    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
//    public void logAfterReturningService(JoinPoint joinPoint, Object result) {
//        log.debug("[Service-AfterReturning] {} | result={}",
//                joinPoint.getSignature().toShortString(), result);
//    }
//
//    @AfterThrowing(pointcut = "serviceMethods()", throwing = "error")
//    public void logAfterThrowingService(JoinPoint joinPoint, Throwable error) {
//        log.error("[Service-Exception] {} | error={}",
//                joinPoint.getSignature().toShortString(), error.getMessage(), error);
//    }

}

package com.cydeo.accounting_app.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {
    @Around("@annotation(com.cydeo.accounting_app.annotation.ExecutionTime)")
    public Object calculateExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object object = null;
        String methodName = proceedingJoinPoint.getSignature().getName();
        log.info("Execution starts:");
        long startTime = System.currentTimeMillis();
        try {
            return proceedingJoinPoint.proceed();
        } catch (Throwable throwable){
           throwable.getStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        log.info("Time taken to execute: {} ms - Method: {}", executionTime, methodName);
        return object;
    }
}
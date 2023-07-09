package com.cydeo.accounting_app.aspect;



import com.cydeo.accounting_app.entity.common.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private UserPrincipal getCompany() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((UserPrincipal) authentication.getPrincipal());
    }
    private String getUserFullNameAndUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((UserPrincipal) authentication.getPrincipal()).getFullNameForProfile()+" "+authentication.getName();
    }

    @Pointcut("execution(* com.cydeo.accounting_app.controller.CompanyController.activateCompany(..))")
    public void loggingCompanyActivation(){}

    @Pointcut("execution(* com.cydeo.accounting_app.controller.CompanyController.deactivateCompany(..))")
    public void loggingCompanyDeactivation(){}

    @AfterReturning(pointcut = "loggingCompanyActivation()", returning = "result")
    public void afterLoggingCompanyActivation(JoinPoint joinPoint, Object result) {
        log.info("After Returning -> Method name: {}, Company Name: {}, Full Name and username of logged-in user: {}",
                joinPoint.getSignature().toShortString(), getCompany().getCompanyTitleForProfile(),
                getUserFullNameAndUserName());
    }
    @AfterReturning(pointcut = "loggingCompanyDeactivation()", returning = "result")
    public void afterLoggingCompanyDeactivation(JoinPoint joinPoint, Object result) {
        log.info("After Returning -> Method name: {}, Company Name: {}, Full Name and username of logged-in user: {}",
                joinPoint.getSignature().toShortString(), getCompany().getCompanyTitleForProfile(),
                getUserFullNameAndUserName());
    }



    @Pointcut("@annotation(com.cydeo.accounting_app.annotation.LoggingAnnotation)")
    public void loggingAnnotationPC() {}

    @Around("loggingAnnotationPC()")
    public Object anyLoggingAnnotationOperation(ProceedingJoinPoint proceedingJoinPoint) {

        long beforeTime = System.currentTimeMillis();
        Object result = null;
        log.info("Execution starts:");

        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        long afterTime = System.currentTimeMillis();

        log.info("Time taken to execute: {} ms - Method: {}"
                , (afterTime - beforeTime), proceedingJoinPoint.getSignature().toShortString());

        return result;

    }


}

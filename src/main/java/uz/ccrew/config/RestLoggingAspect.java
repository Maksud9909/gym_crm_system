package uz.ccrew.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class RestLoggingAspect {
    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logRestCall(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();
        log.info("REST Call: {}.{}, Args: {}",
                className,
                methodName,
                Arrays.toString(joinPoint.getArgs())
        );

        long startTime = System.currentTimeMillis();
        Object result;

        try {
            result = joinPoint.proceed();
            log.info("REST Response: {}.{}, Status: SUCCESS, Time: {} ms",
                    className,
                    methodName,
                    System.currentTimeMillis() - startTime
            );

            return result;
        } catch (Exception e) {
            log.error("REST Response: {}.{}, Status: ERROR, Message: {}",
                    className,
                    methodName,
                    e.getMessage()
            );
            throw e;
        }
    }
}
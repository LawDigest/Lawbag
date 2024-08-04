package com.everyones.lawmaking.global.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class QueryLoggingConfig {
    private static final Logger hibernateSqlLogger = (Logger) LoggerFactory.getLogger("org.hibernate.SQL");

    @Before("execution(* com.everyones.lawmaking.service.DataService.*(..))")
    public void disableQueryLoggingConfig(){
        hibernateSqlLogger.setLevel(Level.OFF);

    }


    @After("execution(* com.everyones.lawmaking.service..*(..)) && !execution(* com.everyones.lawmaking.service.dataservice..*(..))")
    public void enableQueryLoggingConfig() {
        hibernateSqlLogger.setLevel(Level.INFO);
    }
}

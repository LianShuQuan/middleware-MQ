package com.middleware.messagequeue.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class CtxAware implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}


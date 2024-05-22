package com.middleware.messagequeue.util;

import org.springframework.context.ApplicationContext;

public class SpringContextUtil {
    public static ApplicationContext ac;
    public static void setAc(ApplicationContext ac) {
        SpringContextUtil.ac = ac;
    }
    public static <T> T getBean(Class<T> clazz) {
        return ac.getBean(clazz);
    }
}

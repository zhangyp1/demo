package com.newland.paas.sbcommon.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext context = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    // 获取当前环境
    public static String getActiveProfile() {
        return context.getEnvironment().getActiveProfiles()[0];
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        if (context.containsBean(name)) {
            return context.getBean(name, requiredType);
        } else {
            return null;
        }

    }

    public static Object getBean(String name) {
        if (context.containsBean(name)) {
            return context.getBean(name);
        } else {
            return null;
        }

    }

    public static String getApplicationName() {
        return context.getEnvironment().getProperty("spring.application.name");

    }

    public static String getVaue(String key) {
        return context.getEnvironment().getProperty(key);

    }

}

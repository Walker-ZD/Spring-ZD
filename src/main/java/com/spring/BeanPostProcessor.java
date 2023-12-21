package com.spring;

public interface BeanPostProcessor {
    Object postProcessInitialization(Object bean, String beanName);
    Object afterProcessInitialization(Object bean, String beanName);
}

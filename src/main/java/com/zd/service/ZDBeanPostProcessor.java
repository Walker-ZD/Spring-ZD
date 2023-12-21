package com.zd.service;

import com.spring.BeanPostProcessor;
import com.spring.Component;

@Component
public class ZDBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessInitialization(Object bean, String beanName) {
        // 初始化前调用这个方法
        // 针对一个bean或者多个bean同时处理都行
//        if(beanName.equals("userService")){
//            System.out.println("初始化前");
//            ((UserService)bean).setName("userService2");
//        }
        System.out.println("postProcessor");
        return bean;
    }

    @Override
    public Object afterProcessInitialization(Object bean, String beanName) {
        return bean;
    }
}

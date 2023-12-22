package com.zd.service;

import com.spring.BeanPostProcessor;
import com.spring.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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

        // 匹配
        if(beanName.equals("userService")){
            // 使用JDK的动态代理生成一个代理对象
            // 代理的接口是当前这个bean
            Object proxyInstance = Proxy.newProxyInstance(ZDBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("代理逻辑");     // 找切点
                    return method.invoke(bean, args);  // 执行被代理对象原来的方法并返回
                }
            });
            return proxyInstance;
        }

        return bean;
    }
}

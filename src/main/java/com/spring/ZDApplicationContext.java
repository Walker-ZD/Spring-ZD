package com.spring;


public class ZDApplicationContext {
    private Class configClass;


    public ZDApplicationContext(Class configClass) {
        this.configClass = configClass;
    }

    public Object getBean(String beanName){
        return null;
    }
}

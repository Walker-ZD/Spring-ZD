package com.zd.service;

import com.spring.AutoWeired;
import com.spring.BeanNameAware;
import com.spring.Component;
import com.spring.Scope;

@Component("userService")
@Scope("singleton")
public class UserService implements BeanNameAware {

    @AutoWeired
    private OrderService orderService;

    private String beanName;

    @Override
    public void setBeanName(String name) {
        beanName = name;
    }

    public void test(){
        System.out.println(orderService);
        System.out.println(beanName);
    }
}

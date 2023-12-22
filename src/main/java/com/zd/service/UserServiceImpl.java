package com.zd.service;

import com.spring.*;

@Component("userService")
@Scope("singleton")
public class UserServiceImpl implements UserService, BeanNameAware, InitializingBean {

    @AutoWeired
    private OrderService orderService;

    private String beanName;

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setBeanName(String name) {
        beanName = name;
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("Initializing");
    }

    @Override
    public void test() {
        System.out.println(orderService);
        System.out.println(beanName);
    }
}

package com.zd;

import com.spring.ZDApplicationContext;
import com.zd.service.OrderService;
import com.zd.service.UserService;

public class Test {
    public static void main(String[] args) {
        ZDApplicationContext zdApplicationContext = new ZDApplicationContext(AppConfig.class);

        UserService userService = (UserService)zdApplicationContext.getBean("userService");
        OrderService orderService = (OrderService)zdApplicationContext.getBean("orderService");
        userService.test();
    }
}

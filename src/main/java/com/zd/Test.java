package com.zd;

import com.spring.ZDApplicationContext;
import com.zd.service.UserService;

public class Test {
    public static void main(String[] args) {
        ZDApplicationContext zdApplicationContext = new ZDApplicationContext(AppConfig.class);

        Object userService = zdApplicationContext.getBean("userService");
        System.out.println(userService);
    }
}

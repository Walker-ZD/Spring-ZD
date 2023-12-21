package com.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)  // 表示在.class被装载时将被读取，在程序运行期间，将一直保留。
@Target({ElementType.TYPE, ElementType.FIELD}) // FIELD：可以被加在属性或方法上
public @interface AutoWeired {

}

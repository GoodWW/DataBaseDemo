package com.zrw.example.databasedemo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者：张人文
 * 时间：2021/1/26 23:00
 * 邮箱：479696877@QQ.COM
 * 描述：用来控制   列名    叫什么
 * 如果使用了注解   就使用注解上名字作为的  列名
 * 没有使用注解   就使用类名作为  列名
 */

@Target(ElementType.FIELD)//作用在属性上  就是列名
@Retention(RetentionPolicy.RUNTIME)//运行时
public @interface DbField {
    String value();
}

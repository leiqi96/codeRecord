package com.wuanquan.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)     //作用于类
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
    String name();
}

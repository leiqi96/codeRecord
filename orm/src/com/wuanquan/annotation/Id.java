package com.wuanquan.annotation;

import java.lang.annotation.*;

/**
 * description:
 *
 * @author liyazhou
 * @since 2017-07-22 16:44
 */
@Target(ElementType.FIELD)		//����������
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Id {
    String name();
    String type() default "int";
    int length() default 20;
    int increment() default 1;
}

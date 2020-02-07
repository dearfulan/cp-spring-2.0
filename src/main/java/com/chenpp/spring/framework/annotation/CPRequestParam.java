package com.chenpp.spring.framework.annotation;

import java.lang.annotation.*;


/**
 * 请求参数
 *
 * */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CPRequestParam {
    String value() default "";
}

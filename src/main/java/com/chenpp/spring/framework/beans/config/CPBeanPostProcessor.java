package com.chenpp.spring.framework.beans.config;

/**
 * 2020/2/9
 * created by chenpp
 */
public interface CPBeanPostProcessor {

    //为在Bean的初始化前提供回调入口
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    //为在Bean的初始化之后提供回调入口
    default Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

}

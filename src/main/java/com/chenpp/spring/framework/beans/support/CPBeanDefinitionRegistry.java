package com.chenpp.spring.framework.beans.support;

import com.chenpp.spring.framework.beans.config.CPBeanDefinition;

/**
 * 2020/2/6
 * created by chenpp
 */
public interface CPBeanDefinitionRegistry {


    void registerBeanDefinition(String beanName, CPBeanDefinition beanDefinition)  throws Exception;



    CPBeanDefinition getBeanDefinition(String beanName) throws Exception;


    boolean containsBeanDefinition(String beanName);
}

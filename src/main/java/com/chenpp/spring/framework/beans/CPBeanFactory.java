package com.chenpp.spring.framework.beans;

/**
 * 2020/2/6
 * Bean工厂的顶层设计
 * created by chenpp
 */
public interface CPBeanFactory {
    /**
     * 根据bean的名字，获取在IOC容器中得到bean实例
     */
    public Object getBean(String name) throws Exception;
}

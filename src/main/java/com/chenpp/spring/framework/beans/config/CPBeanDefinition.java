package com.chenpp.spring.framework.beans.config;

/**
 * 2020/2/6
 * created by chenpp
 */
public class CPBeanDefinition {

    private String beanClassName;

    private boolean lazyInit = false;

    private boolean singleton = true;

    private String factoryBeanName;

    private boolean abstractFlag;

    public boolean isAbstract() {
        return abstractFlag;
    }

    public void setAbstract(boolean abstractFlag) {
        this.abstractFlag = abstractFlag;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }
}

package com.chenpp.spring.framework.aop.proxy;


/**
 * 2020/2/9
 * created by chenpp
 */
public class CPCglibAopProxy implements  CPAopProxy  {

    private CPAdvisedSupport advised;

    public CPCglibAopProxy(CPAdvisedSupport config){
        this.advised = config;
    }

    /**
     * 创建代理对象
     */
    @Override
    public Object getProxy() {
        return null;
    }

    /**
     * Create a new proxy object.
     *
     * @param classLoader
     * @return the new proxy object
     */
    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }

}

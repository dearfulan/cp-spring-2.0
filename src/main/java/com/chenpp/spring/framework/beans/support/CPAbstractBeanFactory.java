package com.chenpp.spring.framework.beans.support;

import com.chenpp.spring.framework.beans.CPBeanFactory;
import com.chenpp.spring.framework.beans.config.CPBeanDefinition;

/**
 * 2020/2/6
 * created by chenpp
 */
public abstract class CPAbstractBeanFactory implements  CPBeanFactory{

    /**
     * 根据bean的名字，获取在IOC容器中得到bean实例
     *
     * @param name
     */
    @Override
    public Object getBean(String name) throws Exception {
       return doGetBean(name);
    }

    private Object doGetBean(String beanName) {

        //1.实例化
        CPBeanWrapper beanWrapper =  instantiateBean(beanName,new CPBeanDefinition());

        //2、注入
        populateBean(beanName, new CPBeanDefinition(), new CPBeanWrapper());

        return beanWrapper;
    }

    private void populateBean(String beanName, CPBeanDefinition beanDefinition, CPBeanWrapper beanWrapper) {
    }

    private CPBeanWrapper instantiateBean(String beanName, CPBeanDefinition beanDefinition) {
        return null;
    }

    abstract  void preInstantiateSingletons() throws Exception;
}

package com.chenpp.spring.framework.aop.proxy;

import com.chenpp.spring.framework.aop.config.CPAopConfig;
import com.chenpp.spring.framework.beans.config.CPBeanPostProcessor;
import com.chenpp.spring.framework.beans.support.CPListableBeanFactory;

import java.util.Properties;

/**
 * 2020/2/9
 * created by chenpp
 */
@Deprecated
public class CPAutoProxyCreator implements CPBeanPostProcessor {


    private CPListableBeanFactory beanFactory;

    public CPAutoProxyCreator(CPListableBeanFactory beanFactory){
        this.beanFactory = beanFactory;
    }

    //为在Bean的初始化前提供回调入口
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    //为在Bean的初始化之后提供回调入口
    @Override
    public Object postProcessAfterInitialization( Object bean, String beanName) {
        Object proxy ;
        CPAdvisedSupport config = initAdvised(bean);
        if(config.isClassPointcutMatch()){
            proxy =  createAopProxy(config).getProxy(bean.getClass().getClassLoader());
        }else{
            proxy = bean;
        }
        return proxy;
    }
    protected CPListableBeanFactory getBeanFactory() {
        return this.beanFactory;
    }


    private CPAdvisedSupport initAdvised(Object bean){
        //读取配置文件获得aop的配置
        CPAopConfig aopConfig = new CPAopConfig();
        Properties properties = this.getBeanFactory().getReader().getConfig();
        aopConfig.setPointCut(properties.getProperty("pointCut"));
        aopConfig.setAspectClass(properties.getProperty("aspectClass"));
        aopConfig.setBeforeMethod(properties.getProperty("beforeMethod"));
        aopConfig.setAfterMethod(properties.getProperty("afterMethod"));
        aopConfig.setAfterReturnMethod(properties.getProperty("afterReturnMethod"));
        aopConfig.setAroundMethod(properties.getProperty("aroundMethod"));
        aopConfig.setAfterThrowMethod(properties.getProperty("afterThrowMethod"));
        aopConfig.setAfterThrowingName(properties.getProperty("afterThrowingName"));

        CPAdvisedSupport config = new CPAdvisedSupport(aopConfig);
        config.setTarget(bean);
        return config;
    }


    //根据AdvisedSupport判断使用什么代理方式(CGlib还是JDK) 注意还是判断是否有接口实现
    private CPAopProxy createAopProxy(CPAdvisedSupport config) {
        Class targetClass = config.getTargetClass();
        if(targetClass.getInterfaces() != null && targetClass.getInterfaces().length > 0){
            return new CPJdkDynamicAopProxy(config);
        }
        return new CPCglibAopProxy(config);
    }
}

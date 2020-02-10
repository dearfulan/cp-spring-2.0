package com.chenpp.spring.framework.aop.proxy;


import com.chenpp.spring.framework.aop.intercept.CPMethodInvocation;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 2020/2/9
 * created by chenpp
 */
public class CPCglibAopProxy implements  CPAopProxy , MethodInterceptor {

    private CPAdvisedSupport advised;

    public CPCglibAopProxy(CPAdvisedSupport config){
        this.advised = config;
    }

    /**
     * 创建代理对象
     */
    @Override
    public Object getProxy() {
        return getProxy(null);
    }

    /**
     * Create a new proxy object.
     *
     * @param classLoader
     * @return the new proxy object
     */
    @Override
    public Object getProxy(ClassLoader classLoader) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.advised.getTargetClass());
        // 设置回调方法
        enhancer.setCallback(this);
        // 创建代理对象
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object target = this.advised.getTarget();
        Class<?> targetClass = this.advised.getTargetClass();
        Object retVal = null;
        CPMethodInvocation invocation = null;
        List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
        //如果没有可以应用到此方法的Interceptor，直接反射调用 method.invoke(target, args)
        if ( chain == null || chain.isEmpty()) {
            retVal = method.invoke(target, objects);
        }
        else {
            //创建MethodInvocation
            invocation = new CPReflectiveMethodInvocation(o, target, method, objects, targetClass, chain);
            // Proceed to the joinpoint through the interceptor chain.
            retVal = invocation.proceed();

        }
        return retVal;
    }
}

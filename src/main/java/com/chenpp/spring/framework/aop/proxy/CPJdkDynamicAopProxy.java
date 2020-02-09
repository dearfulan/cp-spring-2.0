package com.chenpp.spring.framework.aop.proxy;


import com.chenpp.spring.framework.aop.intercept.CPMethodInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * 2020/2/9
 * created by chenpp
 */
public class CPJdkDynamicAopProxy implements CPAopProxy , InvocationHandler {

    private CPAdvisedSupport advised;

    public CPJdkDynamicAopProxy(CPAdvisedSupport config){
        this.advised = config;
    }
    /**
     * 创建代理对象
     */
    @Override
    public Object getProxy() {
        return getProxy(this.advised.getTargetClass().getClassLoader());
    }

    /**
     * Create a new proxy object.
     *
     * @param classLoader
     * @return the new proxy object
     */
    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader,this.advised.getTargetClass().getInterfaces(),this);
    }

    /**
     * 代理对象执行方法实实际调用的代码
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object target = this.advised.getTarget();
        Class<?> targetClass = this.advised.getTargetClass();
        Object retVal = null;
        CPMethodInvocation invocation = null;
        List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
        //如果没有可以应用到此方法的Interceptor，直接反射调用 method.invoke(target, args)
        if ( chain == null || chain.isEmpty()) {
            retVal = method.invoke(target, args);
        }
        else {
            //创建MethodInvocation
            invocation = new CPReflectiveMethodInvocation(proxy, target, method, args, targetClass, chain);
            // Proceed to the joinpoint through the interceptor chain.
            retVal = invocation.proceed();

        }
        return retVal;
    }
}

package com.chenpp.spring.framework.aop.proxy;

import com.chenpp.spring.framework.aop.intercept.CPMethodInterceptor;
import com.chenpp.spring.framework.aop.intercept.CPMethodInvocation;
import org.omg.PortableInterceptor.Interceptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2020/2/9
 * created by chenpp
 */
public class CPReflectiveMethodInvocation implements CPMethodInvocation {

    private Object proxy ;
    private Object target ;
    private Method method ;
    private Object[] arguments;
    private Class<?> targetClass;
    private List<Object> interceptorsAndDynamicMethodMatchers;

    private Map<String,Object> userAttributes;




    /**
     * Index from 0 of the current interceptor we're invoking.
     * 标记当前interceptor链执行到的下标
     */
    private int currentInterceptorIndex = -1;


    //存储当前代理对象和源对象的各种信息
    public CPReflectiveMethodInvocation(Object proxy, Object target, Method method, Object[] arguments,
                                     Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers) {

        this.proxy = proxy;
        this.target = target;
        this.targetClass = targetClass;
        this.method = method;
        this.arguments = arguments;
        //保存当前的interceptor链
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
    }

    public Object proceed() throws Throwable {
        //如果Interceptor执行完了，则执行joinPoint
        if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
            return this.method.invoke(this.target, this.arguments);
        }

        Object interceptor = this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
        //判断interceptor是否为MethodInterceptor (spring还有个Matcher来判断是否匹配,这边简化)
        if (interceptor instanceof CPMethodInterceptor) {
            CPMethodInterceptor mi = (CPMethodInterceptor) interceptor;
            //执行当前Intercetpor
            return mi.invoke(this);
        }
        else {
            //循环往下执行(即获取下一个interceptor执行)
            return proceed();
        }
    }

    @Override
    public Object getThis() {
        return this.target;
    }


    public Object getProxy() {
        return proxy;
    }


    /**
     * set the value of the specified user attribute
     *
     * @param key
     * @param value
     */
    @Override
    public void setUserAttribute(String key, Object value) {
        if(userAttributes == null){
            userAttributes = new HashMap<>();
        }
        userAttributes.put(key,value);
    }

    /**
     * Return the value of the specified user attribute.
     *
     * @param key
     */
    @Override
    public Object getUserAttribute(String key) {
        if( userAttributes == null){
            return null;
        }
        return userAttributes.get(key);
    }

    public Object getTarget() {
        return target;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public List<Object> getInterceptorsAndDynamicMethodMatchers() {
        return interceptorsAndDynamicMethodMatchers;
    }
}

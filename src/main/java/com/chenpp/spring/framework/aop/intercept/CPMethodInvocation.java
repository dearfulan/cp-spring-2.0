package com.chenpp.spring.framework.aop.intercept;

import java.lang.reflect.Method;

/**
 * 2020/2/9
 * created by chenpp
 */
public interface  CPMethodInvocation  extends  CPJoinpoint{

    /**
     * Return the proxy
     */
    Object getProxy();

    /**
     *  set the value of the specified user attribute
     */
    void setUserAttribute(String key, Object value);

    /**
     * Return the value of the specified user attribute.
     */
    Object getUserAttribute(String key);

    Method getMethod();

    Object[] getArguments();
}

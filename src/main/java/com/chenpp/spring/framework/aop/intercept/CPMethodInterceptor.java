package com.chenpp.spring.framework.aop.intercept;

/**
 * 2020/2/9
 * created by chenpp
 */
public interface CPMethodInterceptor {

    Object invoke(CPMethodInvocation invocation) throws Throwable;

}

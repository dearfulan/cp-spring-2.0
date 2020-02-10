package com.chenpp.spring.framework.aop.intercept;

import com.chenpp.spring.framework.aop.aspect.CPAbstractAspectjAdvice;

import java.lang.reflect.Method;

/**
 * 2020/2/9
 * created by chenpp
 */
public class CPMethodAroundAdviceInterceptor extends CPAbstractAspectjAdvice implements  CPMethodInterceptor{


    private CPJoinpoint joinpoint ;

    public CPMethodAroundAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod,aspectTarget);
    }

    public Object around(Method method, Object[] arguments, Object target) throws Throwable {
        return invokeAdviceMethod(joinpoint, null, null);
    }


    @Override
    public Object invoke(CPMethodInvocation invocation) throws Throwable {
        this.joinpoint = invocation;
        return this.around(invocation.getMethod(), invocation.getArguments(), invocation.getThis());
    }
}

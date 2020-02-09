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

    public void after(Method method, Object[] arguments, Object target) throws Throwable {
        invokeAdviceMethod(joinpoint, null, null);
    }

    @Override
    public Object invoke(CPMethodInvocation invocation) throws Throwable {
        //TODO 这里的写法有问题 稍后完善  或者看看别人的
        this.joinpoint = invocation;
        try {
            return invocation.proceed();
        }
        finally {
            this.after(invocation.getMethod(), invocation.getArguments(), invocation.getThis() );
        }
    }
}

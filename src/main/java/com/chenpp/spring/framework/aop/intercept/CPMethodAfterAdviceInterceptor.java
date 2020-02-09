package com.chenpp.spring.framework.aop.intercept;

import com.chenpp.spring.framework.aop.aspect.CPAbstractAspectjAdvice;

import java.lang.reflect.Method;

/**
 * 2020/2/9
 * created by chenpp
 */
public class CPMethodAfterAdviceInterceptor extends CPAbstractAspectjAdvice implements  CPMethodInterceptor{


    private CPJoinpoint joinpoint ;

    public CPMethodAfterAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod,aspectTarget);
    }

    public void after(Method method, Object[] arguments, Object target) throws Throwable {
        invokeAdviceMethod(joinpoint, null, null);
    }

    @Override
    public Object invoke(CPMethodInvocation invocation) throws Throwable {
        this.joinpoint = invocation;
        try {
            return invocation.proceed();
        }
        finally {
            this.after(invocation.getMethod(), invocation.getArguments(), invocation.getThis() );
        }
    }
}

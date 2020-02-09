package com.chenpp.spring.framework.aop.intercept;

import com.chenpp.spring.framework.aop.aspect.CPAbstractAspectjAdvice;

import java.lang.reflect.Method;

/**
 * 2020/2/9
 * created by chenpp
 * 我们执行的时候是使用interceptor来执行对应的切面方法
 * 所以在构造对应的interceptor的时候就需要把对应的切面方法传入
 */
public class CPMethodAfterReturningAdviceInterceptor extends CPAbstractAspectjAdvice implements CPMethodInterceptor {


    private CPJoinpoint joinpoint ;

    public CPMethodAfterReturningAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod,aspectTarget);
    }

    public void afterReturning(Object returnValue,Method method, Object[] arguments, Object target) throws Throwable {
        invokeAdviceMethod(joinpoint, returnValue, null);
    }

    @Override
    public Object invoke(CPMethodInvocation invocation) throws Throwable {
        //这里的参数是调用代理方法的参数
        //我们还需要转化成对应的切面方法的参数才可以执行
        this.joinpoint = invocation;
        Object returnVal = invocation.proceed();;
        this.afterReturning(returnVal,invocation.getMethod(),invocation.getArguments(),invocation.getThis() );
        return returnVal;
    }
}

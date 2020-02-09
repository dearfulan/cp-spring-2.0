package com.chenpp.spring.framework.aop.intercept;

import com.chenpp.spring.framework.aop.aspect.CPAbstractAspectjAdvice;

import java.lang.reflect.Method;

/**
 * 2020/2/9
 * created by chenpp
 */
public class CPMethodAfterThrowAdviceInterceptor extends CPAbstractAspectjAdvice implements  CPMethodInterceptor{


    private CPJoinpoint joinpoint ;

    public CPMethodAfterThrowAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod,aspectTarget);
    }

    public void after(Method method, Object[] arguments, Object target) throws Throwable {
        invokeAdviceMethod(joinpoint, null, null);
    }

    @Override
    public Object invoke(CPMethodInvocation invocation) throws Throwable {
        //如果对应的方法执行出现异常,则执行我们自己定义的方法
        try {
            this.joinpoint = invocation ;
            return invocation.proceed();
        }catch (Throwable e){
            invokeAdviceMethod(joinpoint,null,e.getCause());
            throw e;
        }
    }
}

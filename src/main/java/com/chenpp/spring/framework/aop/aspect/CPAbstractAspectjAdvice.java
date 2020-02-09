package com.chenpp.spring.framework.aop.aspect;

import com.chenpp.spring.framework.aop.intercept.CPJoinpoint;

import java.lang.reflect.Method;

/**
 * 2020/2/9
 * created by chenpp
 */
public abstract class CPAbstractAspectjAdvice implements CPAdvice {

    private  Method aspectMethod;

    private Object aspectTarget;

    //切面方法和切面对象
    public CPAbstractAspectjAdvice(Method aspectMethod,Object aspectTarget){
        this.aspectMethod = aspectMethod ;
        this.aspectTarget = aspectTarget ;
    }


    protected Object invokeAdviceMethod(CPJoinpoint joinpoint, Object returnValue, Throwable ex) throws Throwable {
        Class<?>[] paramTypes = aspectMethod.getParameterTypes();
        if(null == paramTypes || paramTypes.length == 0){
            return this.aspectMethod.invoke(aspectTarget);
        }
        Object[] args = new Object[paramTypes.length];
        //对于切面的参数赋值,一般也就jointpoint,returnValue,异常三个
        for(int i = 0 ; i < paramTypes.length ; i++ ){
            if(paramTypes[i] == CPJoinpoint.class){
                args[i] = joinpoint ;
            }else if(paramTypes[i] == Object.class){
                args[i] = returnValue ;
            }else if(paramTypes[i] == Exception.class || paramTypes[i] == Throwable.class){
                args[i] = ex;
            }
        }
        aspectMethod.invoke(aspectTarget,args);
        return null;
    }
}

package com.chenpp.spring.demo.aspect;

import com.chenpp.spring.framework.aop.intercept.CPJoinpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.SchemaOutputResolver;
import java.util.Arrays;

/**
 * Created by chenpp
 */
public class LogAspect {

    private static Logger log = LoggerFactory.getLogger(LogAspect.class);

    //在调用一个方法之前，执行before方法
    public void before(CPJoinpoint joinPoint){
        joinPoint.setUserAttribute("startTime_" + joinPoint.getMethod().getName(),System.currentTimeMillis());
        //这个方法中的逻辑，是由我们自己写的
        log.info("Invoker Before Method!!!" + "TargetObject:" +  joinPoint.getThis() + "Args:" + Arrays.toString(joinPoint.getArguments()));
    }

    //在调用一个方法之后，执行after方法
    public void after(CPJoinpoint joinPoint){
        log.info("Invoker After Method!!!" + "TargetObject:" +  joinPoint.getThis() + "Args:" + Arrays.toString(joinPoint.getArguments()));
        long startTime = (Long) joinPoint.getUserAttribute("startTime_" + joinPoint.getMethod().getName());
        long endTime = System.currentTimeMillis();
        log.info("this method use time :" + (endTime - startTime));
    }

    /**
     * 在调用一个方法前后执行around方法
     * 如果想要拿到返回值，around需要返回proceed()执行结果
     */
    public Object around(CPJoinpoint joinPoint) throws Throwable {
        log.info("Invoker around before Method!!!" + "TargetObject:" +  joinPoint.getThis() + "Args:" + Arrays.toString(joinPoint.getArguments()));
        Object returnVal  = joinPoint.proceed();
        log.info("Invoker around after Method!!!" + "TargetObject:" +  joinPoint.getThis() + "Args:" + Arrays.toString(joinPoint.getArguments()));
        return  returnVal;
    }

    public void afterReturning(CPJoinpoint joinPoint, Object  returnValue){

        log.info("Invoker afterReturning Method!!!返回值为 :"+ returnValue);
    }


    public void afterThrowing(CPJoinpoint joinPoint, Throwable ex){
        log.info("出现异常" + " TargetObject:" +  joinPoint.getThis() + " Args:" + Arrays.toString(joinPoint.getArguments()) + " Throws:" + ex.getMessage());
    }

}

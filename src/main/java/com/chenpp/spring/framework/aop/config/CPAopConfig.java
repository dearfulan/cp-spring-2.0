package com.chenpp.spring.framework.aop.config;


/**
 * 2020/2/9
 * created by chenpp
 */
public class CPAopConfig {
    //切面表达式
    private String pointCut;
    //切面类
    private String aspectClass;
    private String beforeMethod;
    private String afterMethod;
    private String afterReturnMethod;
    private String afterThrowMethod;
    private String aroundMethod;
    private String afterThrowingName;
    private Object aspectInstance;

    public String getPointCut() {
        return pointCut;
    }

    public void setPointCut(String pointCut) {
        this.pointCut = pointCut;
    }

    public String getAspectClass() {
        return aspectClass;
    }

    public void setAspectClass(String aspectClass) {
        this.aspectClass = aspectClass;
        try {
            Class<?> clazz = Class.forName(aspectClass);
            this.aspectInstance = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getAspectInstance() {
        return aspectInstance;
    }

    public String getBeforeMethod() {
        return beforeMethod;
    }

    public String getAfterReturnMethod() {
        return afterReturnMethod;
    }

    public void setAfterReturnMethod(String afterReturnMethod) {
        this.afterReturnMethod = afterReturnMethod;
    }

    public void setBeforeMethod(String beforeMethod) {
        this.beforeMethod = beforeMethod;
    }

    public String getAfterMethod() {
        return afterMethod;
    }

    public void setAfterMethod(String afterMethod) {
        this.afterMethod = afterMethod;
    }

    public String getAfterThrowMethod() {
        return afterThrowMethod;
    }

    public void setAfterThrowMethod(String afterThrowMethod) {
        this.afterThrowMethod = afterThrowMethod;
    }

    public String getAroundMethod() {
        return aroundMethod;
    }

    public void setAroundMethod(String aroundMethod) {
        this.aroundMethod = aroundMethod;
    }

    public String getAfterThrowingName() {
        return afterThrowingName;
    }

    public void setAfterThrowingName(String afterThrowingName) {
        this.afterThrowingName = afterThrowingName;
    }
}

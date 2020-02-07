package com.chenpp.spring.framework.beans.support;

/**
 * 2020/2/6
 * created by chenpp
 */
public class CPBeanWrapper {

    private Object wrappedInstance;

    public CPBeanWrapper(Object wrappedInstance){
        this.wrappedInstance = wrappedInstance;

    }

    public Object getWrappedInstance() {
        return wrappedInstance;
    }

    public void setWrappedInstance(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }

    public Class<?> getWrappedClass(){
        return wrappedInstance.getClass();
    }
}

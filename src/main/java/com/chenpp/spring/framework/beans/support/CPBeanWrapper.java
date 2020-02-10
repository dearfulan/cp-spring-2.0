package com.chenpp.spring.framework.beans.support;

/**
 * 2020/2/6
 * created by chenpp
 */
public class CPBeanWrapper {

    private Object wrappedInstance;

    private Class<?> wrappedClass ;

    public CPBeanWrapper(Object wrappedInstance){
        this.wrappedInstance = wrappedInstance;

    }

    public void setWrappedClass(Class<?> wrappedClass) {
        //需要设置这个是因为对于代理类,需要保存原始的class
        this.wrappedClass = wrappedClass;
    }

    public Object getWrappedInstance() {
        return wrappedInstance;
    }

    public void setWrappedInstance(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }

    public Class<?> getWrappedClass(){
        if( this.wrappedClass == null){
            return this.getWrappedInstance().getClass();
        }
        return this.wrappedClass;

    }
}

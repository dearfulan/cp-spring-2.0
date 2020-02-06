package com.chenpp.spring.framework.context;

import com.chenpp.spring.framework.beans.CPBeanFactory;


/**
 * 2020/2/6
 * ApplicationContext的顶层设计
 * created by chenpp
 */
public interface CPApplicationContext extends  CPBeanFactory {


    CPBeanFactory getBeanFactory() throws IllegalStateException;

}

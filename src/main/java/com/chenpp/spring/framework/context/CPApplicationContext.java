package com.chenpp.spring.framework.context;

import com.chenpp.spring.framework.beans.CPBeanFactory;
import com.chenpp.spring.framework.beans.support.CPListableBeanFactory;

import java.util.Properties;


/**
 * 2020/2/6
 * ApplicationContext的顶层设计
 * created by chenpp
 */
public interface CPApplicationContext extends  CPBeanFactory {


    CPListableBeanFactory getBeanFactory() throws IllegalStateException;

    Properties getConfig() ;


}

package com.chenpp.spring.framework.beans.support;

import com.chenpp.spring.framework.beans.CPBeanFactory;

/**
 * 2020/2/7
 * created by chenpp
 */
public interface CPListableBeanFactory extends CPBeanFactory {

    String[] getBeanDefinitionNames();

    String[] getBeanNamesForType(Class<?> type);
}

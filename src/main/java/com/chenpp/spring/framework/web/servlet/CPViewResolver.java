package com.chenpp.spring.framework.web.servlet;

import java.util.Locale;

/**
 * 2020/2/8
 * created by chenpp
 */
public interface CPViewResolver {

    CPView resolveViewName(String viewName, Locale locale) throws Exception;

}

package com.chenpp.spring.framework.util;

import com.chenpp.spring.framework.web.servlet.CPDispatcherServlet;

/**
 * 2020/2/8
 * created by chenpp
 */
public class Constants {

    public static final String RESPONSE_NODY = CPDispatcherServlet.class.getName() + ".RESPONSE_NODY";

    //如果返回的是ResponseBody,返回一个默认的viewName,区分于那些没有设置viewName的
    public final static String DEFAULT_VIEW_NAME = ".DEFAULT_VIEW";

    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=ISO-8859-1";



}

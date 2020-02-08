package com.chenpp.spring.framework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 2020/2/8
 * created by chenpp
 */
public interface CPHandlerAdapter {

    boolean supports(Object handler);

    CPModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;

}

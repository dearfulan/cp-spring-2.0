package com.chenpp.spring.framework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 2020/2/8
 * created by chenpp
 */
public interface CPView {

    void render(Map<String, ?> model,  HttpServletRequest request, HttpServletResponse response) throws Exception;

}

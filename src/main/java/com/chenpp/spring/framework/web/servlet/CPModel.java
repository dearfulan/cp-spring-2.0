package com.chenpp.spring.framework.web.servlet;

import java.util.Map;

/**
 * 2020/2/8
 * created by chenpp
 */
public interface CPModel {



    CPModel addAttribute(String attributeName,  Object attributeValue);


    CPModel addAllAttributes(Map<String, Object> attributes);
}

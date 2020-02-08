package com.chenpp.spring.framework.web.servlet;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 2020/2/8
 * created by chenpp
 */
public class CPModelMap extends LinkedHashMap<String, Object> implements CPModel {

    public CPModelMap(){

    }

    public CPModelMap addAttribute(java.lang.String attributeName, java.lang.Object attributeValue) {
        put(attributeName,attributeValue);
        return this;
    }

    public CPModelMap addAllAttributes(Map<java.lang.String, java.lang.Object> attributes) {
        putAll(attributes);
        return this;
    }
}

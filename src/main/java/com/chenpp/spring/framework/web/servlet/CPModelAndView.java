package com.chenpp.spring.framework.web.servlet;

import java.util.Map;

/**
 * 2020/2/8
 * created by chenpp
 */
public class CPModelAndView {

    private String viewName;
    private CPModelMap model ;


    public CPModelAndView() { }

    public CPModelAndView(String viewName) {
        this.viewName = viewName;
    }


    public String getViewName() {
        return viewName;
    }

    public CPModelAndView(String viewName, Map<String,Object> model) {
        this.viewName = viewName;
        this.model = getModelMap().addAllAttributes(model);
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public CPModelAndView addAllObjects(Map<String, Object> modelMap) {
        getModelMap().addAllAttributes(modelMap);
        return this;
    }

    public CPModelAndView addObject(String attributeName, Object attributeValue) {
        getModelMap().addAttribute(attributeName, attributeValue);
        return this;
    }

    public Object getAttribute(String attributeName) {
        getModelMap().get(attributeName);
        return this;
    }

    public CPModelMap getModelMap() {
        if (this.model == null) {
            this.model = new CPModelMap();
        }
        return this.model;
    }


}

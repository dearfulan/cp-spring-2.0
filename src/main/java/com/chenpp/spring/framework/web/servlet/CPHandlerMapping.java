package com.chenpp.spring.framework.web.servlet;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * 2020/2/8
 * created by chenpp
 */
public class CPHandlerMapping {
    private Method method;
    private Object controller;
    private Pattern pattern;

    public CPHandlerMapping(Pattern pattern, Method method, Object controller ) {
        this.method = method;
        this.controller = controller;
        this.pattern = pattern;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

}

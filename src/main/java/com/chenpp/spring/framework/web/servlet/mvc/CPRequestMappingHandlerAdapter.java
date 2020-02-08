package com.chenpp.spring.framework.web.servlet.mvc;

import com.chenpp.spring.framework.annotation.CPRequestParam;
import com.chenpp.spring.framework.annotation.CPResponseBody;
import com.chenpp.spring.framework.util.Constants;
import com.chenpp.spring.framework.util.StringUtils;
import com.chenpp.spring.framework.web.servlet.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 2020/2/8
 * created by chenpp
 */
public class CPRequestMappingHandlerAdapter implements CPHandlerAdapter {


    @Override
    public boolean supports(Object handler) {
        return handler instanceof CPHandlerMapping;
    }

    @Override
    public CPModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        CPHandlerMapping hm = (CPHandlerMapping) handler;
        Method method = hm.getMethod();
        //1.根据方法的Method,获取对应的参数列表,以及各个参数对应的下标
        Map<String,Integer> paramIndexMapping  = getParamIndexMapping(method);

        //2.根据request的参数,获取方法的实参
        Object[] args = getRequestArgs(paramIndexMapping,method,request,response);

        //3.执行方法获得返回值
        Object returnValue = hm.getMethod().invoke(hm.getController(),args);

        //4.根据返回的值实例化ModeAndView
        //先拿到args里的传入的Model和Map参数
        Map map = null;
        for( Object arg : args ){
            if( arg instanceof  Map || arg instanceof CPModelMap ){
                map = (Map) arg;
                break;
            }
        }
        CPModelAndView mv = getModelAndView( returnValue , method , map);
        return mv;
    }

    /**
     * spring的方法返回有三种情形
     * 1.直接返回ModeAndView
     * 2.返回ViewName , Model参数通过方法的Model或者Map来传入
     * 3.添加ResponseBody注解,直接输出到页面
     * 4.如果没有添加ResponseBody注解，也没有返回ViewName,spring会通过RequestToViewNameTranslator从request找到请求的url作为
     *   viewName
     * 5.如果返回的是Map或者Model则会将其作为ModelAndView里的model存入,viewName通过上述方式来寻找
     * */
    private CPModelAndView getModelAndView(Object returnValue, Method method ,Map map) throws Exception {
        if( returnValue == null || returnValue instanceof  Void){
            return null;
        }
        //1.如果返回的是ModelAndView
        if( returnValue instanceof CPModelAndView){
            return (CPModelAndView) returnValue;
        }

        //2.如果method上有ResponseBody注解
        else if( method.isAnnotationPresent(CPResponseBody.class) ){
            CPModelAndView modeAndView = new CPModelAndView(Constants.DEFAULT_VIEW_NAME);
            modeAndView.addObject(Constants.RESPONSE_NODY,returnValue);
            return modeAndView;
        }
        //3.返回视图名
        else if( returnValue instanceof String){
            CPModelAndView modelAndView  = new CPModelAndView((String) returnValue);
            if(map != null){
                modelAndView.addAllObjects(map);
            }
            return modelAndView;
        }
        //4.直接返回model  Model或者Map
        else if( returnValue instanceof CPModel || returnValue instanceof  Map) {
            return new CPModelAndView( null,(Map<String, Object>) returnValue);
        }
        throw new Exception("no View And no ResponseBody , this method is error ");
    }


    private Object[] getRequestArgs(Map<String, Integer> paramIndexMapping, Method method,HttpServletRequest request ,HttpServletResponse response) {
        Object [] args = new Object[method.getParameterCount()];
        Class<?>[] paramTypes = method.getParameterTypes();
        //获取请求的参数列表(Request请求里的参数都是字符串类型的，如果一个参数出现多次，那么它的value就是String数组)
        Map<String,String[]> params = request.getParameterMap();
        for (Map.Entry<String, String[]> param : params.entrySet()) {
            //将数组参数转化为string
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");
            //如果找到匹配的参数名，则开始填充参数数组paramValues
            if( !paramIndexMapping.containsKey(param.getKey()) ){ continue; }
            int index = paramIndexMapping.get(param.getKey());
            //将参数从String转化成对应的形参类型
            args[index] = convert(paramTypes[index],value);
        }
        //对于request,response进行赋值
        if(paramIndexMapping.containsKey(HttpServletRequest.class.getName())) {
            int reqIndex = paramIndexMapping.get(HttpServletRequest.class.getName());
            args[reqIndex] = request;
        }
        if(paramIndexMapping.containsKey(HttpServletResponse.class.getName())) {
            int respIndex = paramIndexMapping.get(HttpServletResponse.class.getName());
            args[respIndex] = response;
        }
        //如果有model和map的参数,自己初始化一个，让方法去调用
        if(paramIndexMapping.containsKey(CPModel.class.getName())){
            int modelIndex = paramIndexMapping.get(CPModel.class.getName());
            CPModelMap modelMap = new CPModelMap();
            args[modelIndex] = modelMap;
        }
        if(paramIndexMapping.containsKey(Map.class.getName())){
            int modelIndex = paramIndexMapping.get(Map.class.getName());
            Map<String,Object> hashMap = new HashMap<>();
            args[modelIndex] = hashMap;
        }
        return args;
    }

    private Object convert(Class<?> paramType, String value) {
        //这里只是列举了几种常用的 String , int , double
        if( String.class == paramType){
            return value;
        }else if( int.class == paramType || Integer.class == paramType){
            return Integer.valueOf(value);
        }
        if( double.class == paramType || Double.class == paramType){
            return Double.valueOf(value);
        }

        return value;
    }

    private  Map<String,Integer> getParamIndexMapping(Method method) {
        Map<String,Integer> paramIndexMapping = new HashMap<String,Integer>();
        //遍历Method中的所有参数,获取其对应的参数名和下标
        Parameter[] params = method.getParameters();
        for( int i = 0 ; i < params.length ; i++){
            Parameter parameter = params[i];
            if (parameter.getType() == HttpServletRequest.class ){
                paramIndexMapping.put(HttpServletRequest.class.getName(),i);
                continue;
            }
            if (parameter.getType() == HttpServletResponse.class ){
                paramIndexMapping.put(HttpServletResponse.class.getName(),i);
                continue;
            }
            if (parameter.getType() == CPModel.class ){
                paramIndexMapping.put(CPModel.class.getName(),i);
                continue;
            }
            if (parameter.getType() == HashMap.class || parameter.getType() == Map.class ){
                paramIndexMapping.put(Map.class.getName(),i);
                continue;
            }
            if( parameter.isAnnotationPresent(CPRequestParam.class)){
                String paramName = parameter.getAnnotation(CPRequestParam.class).value();
                if(!StringUtils.isEmpty(paramName)) {
                    paramIndexMapping.put(paramName, i);
                    continue;
                }
            }
            paramIndexMapping.put(parameter.getName(),i);
        }
        return paramIndexMapping;
    }
}

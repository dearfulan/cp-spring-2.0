package com.chenpp.spring.framework.aop.proxy;

import com.chenpp.spring.framework.aop.config.CPAopConfig;
import com.chenpp.spring.framework.aop.intercept.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 2020/2/9
 * created by chenpp
 */
public class CPAdvisedSupport {

    //做一个缓存
    private transient Map<Method, List<Object>> methodCache;

    public CPAdvisedSupport(CPAopConfig config){
        this.methodCache = new ConcurrentHashMap<>(32);
        this.config = config;
        parseAdvise();

    }


    private CPAopConfig config;

    private Class<?> targetClass;

    private Object target ;

    public Class<?> getTargetClass() {
        return targetClass;
    }


    Pattern pointCutClassPattern ;

    public Object getTarget() {
        return target;
    }

    public CPAopConfig getConfig() {
        return config;
    }

    public void setConfig(CPAopConfig config) {
        this.config = config;
    }

    public void setTarget(Object target) {
        this.target = target;
        this.targetClass = target.getClass();
        try {
            initMethodChains();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //根据目前对象的method和TargetClass获取调用链
    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method,Class<?> targetClass) throws Exception {
        //因为在实例化的时候就对MethodCache进行初始化了 (spring是在获取的时候初始化,这里简化)
        List<Object> cached = this.methodCache.get(method);
        if(cached == null){
            //因为这里的method可能是接口的方法名
            Method m = targetClass.getMethod(method.getName(),method.getParameterTypes());
            cached = this.methodCache.get(m);
        }
        return cached;
    }

    //根据方法,method,targetClass来找到对应的调用链(前置通知,后置通知等调用链)
    //为了获得调用链,需要取得相关的配置信息,这里直接传入一个Config参数
    //实际spring是可以通过xml或者注解配置多个切面的,这里properties就简单配置一个
    //这里就不考虑各个切面的执行顺序,按照默认的顺序来
    private void initMethodChains() throws ClassNotFoundException {
        //获取切面类的方法集合
        Class aspectClass = Class.forName(this.getConfig().getAspectClass());

        Map<String, Method> aspectMethods = new HashMap<String, Method>();
        for (Method m : aspectClass.getMethods()) {
            aspectMethods.put(m.getName(), m);
        }
        String pointCut = this.config.getPointCut()
                .replaceAll("\\.","\\\\.")
                .replaceAll("\\\\.\\*",".*")
                .replaceAll("\\(","\\\\(")
                .replaceAll("\\)","\\\\)");
        //遍历目标的方法集合,判断是否满足切面条件
        Pattern pattern = Pattern.compile(pointCut);
        for (Method m : this.targetClass.getMethods()) {
            List<Object> advices = new LinkedList<Object>();
            String methodString = m.toString();
            if (methodString.contains("throws")) {
                methodString = methodString.substring(0, methodString.lastIndexOf("throws")).trim();
            }
            Matcher matcher = pattern.matcher(methodString);

            if(matcher.matches()){
                //获取对应方法的执行器链
                if(this.config.getBeforeMethod() != null ){
                    advices.add(new CPMethodBeforeAdviceInterceptor(aspectMethods.get(this.config.getBeforeMethod()),this.config.getAspectInstance()) );
                }
                if(this.config.getAfterMethod() != null){
                    advices.add(new CPMethodAfterAdviceInterceptor(aspectMethods.get(this.config.getAfterMethod()),this.config.getAspectInstance()) );
                }
                if(this.config.getAfterReturnMethod() != null){
                    advices.add(new CPMethodAfterReturningAdviceInterceptor(aspectMethods.get(this.config.getAfterReturnMethod()),this.config.getAspectInstance()) );
                }
                if(this.config.getAroundMethod() != null){
                    advices.add(new CPMethodAroundAdviceInterceptor(aspectMethods.get(this.config.getAroundMethod()),this.config.getAspectInstance()) );
                }
                if(this.config.getAfterThrowMethod() != null){
                    advices.add(new CPMethodAfterThrowAdviceInterceptor(aspectMethods.get(this.config.getAfterThrowMethod()),this.config.getAspectInstance()) );
                }
                methodCache.put(m,advices);
            }
        }
    }

    //判断当前class是否满足pointcut表达式子
    public boolean isClassPointcutMatch(){
        return pointCutClassPattern.matcher(this.getTargetClass().toString()).matches();
    }

    public void parseAdvise(){
        String pointcutExpress = this.getConfig().getPointCut();
        //关于这里的正则解析,感觉有奇怪的地方,后面再看吧
        String pointCut = pointcutExpress
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\\\.\\*", ".*")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)");
        //pointCut=public .* com.chenpp.spring.demo.service..*Service..*(.*)
        String pointCutForClassRegex = pointCut.substring(0, pointCut.lastIndexOf("\\(") - 4);
        pointCutClassPattern = Pattern.compile("class " + pointCutForClassRegex.substring(pointCutForClassRegex.lastIndexOf(" ") + 1));

    }
}

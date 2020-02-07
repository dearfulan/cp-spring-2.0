package com.chenpp.spring.framework.beans.support;

import com.chenpp.spring.framework.annotation.CPAutowire;
import com.chenpp.spring.framework.annotation.CPController;
import com.chenpp.spring.framework.annotation.CPQualifier;
import com.chenpp.spring.framework.annotation.CPService;
import com.chenpp.spring.framework.beans.CPBeanFactory;
import com.chenpp.spring.framework.beans.config.CPBeanDefinition;
import com.chenpp.spring.framework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 2020/2/6
 * created by chenpp
 */
public abstract class CPAbstractBeanFactory implements  CPListableBeanFactory{

    //单例Bean的缓存
    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>(16);

    //缓存Bean实例,ioc容器
    private final Map<String, CPBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>(16);


    /**
     * 根据bean的名字，获取在IOC容器中得到bean实例
     *
     * @param name
     */
    @Override
    public Object getBean(String name) throws Exception {
       return doGetBean(name);
    }


    private Object doGetBean(String beanName) throws Exception{
        //1.获取之前定义的BeanDefinition
        CPBeanDefinition beanDefinition = this.getBeanDefinition(beanName);

        //2.实例化得到BeanWrapper
        Object bean =  instantiateBean(beanName,beanDefinition);
        CPBeanWrapper beanWrapper = new CPBeanWrapper(bean);

        //3.将BeanWrapper保存到ioc容器中(对于prototype类型的不需要,因为每次都要创建)
        if(beanDefinition.isSingleton() && !factoryBeanInstanceCache.containsKey(beanName)){
            this.factoryBeanInstanceCache.put(beanName,beanWrapper);
        }

        //TODO 如果是依赖注入怎么办 ?? 我有个问题,在实例化之后需要再进行依赖注入---这里应该需要判断该实例是否已经完成依赖注入

        //4、注入
        populateBean(beanName,beanDefinition, beanWrapper);
        Object exposedObject = bean;

        //5.初始化Bean对象,为Bean实例对象添加BeanPostProcessor后置处理器
        exposedObject = initializeBean(beanName, exposedObject, beanDefinition);

        return exposedObject;
    }



    private void populateBean(String beanName, CPBeanDefinition beanDefinition, CPBeanWrapper beanWrapper) throws Exception {
        Object instance = beanWrapper.getWrappedInstance();
        Class<?> clazz = beanWrapper.getWrappedClass();
        //判断是否加了CPController,CPService注解 这里不考虑Resource等其他注解
        if( !(clazz.isAnnotationPresent(CPController.class) || clazz.isAnnotationPresent(CPService.class)) ){
            return;
        }
        //反射获得所有的fields
        Field[] fields = clazz.getDeclaredFields();
        for( Field field : fields){
            if(!field.isAnnotationPresent(CPAutowire.class)){  continue; }
            field.setAccessible(true);
            //对于使用了CPAutowire注解判断是否有使用CPQualifier指定beanName
            CPQualifier qualifier = field.getAnnotation(CPQualifier.class);
            if(qualifier != null && !StringUtils.isEmpty(qualifier.value())){
                //按照名字注入
                CPBeanWrapper autowireBean = factoryBeanInstanceCache.get(qualifier.value());
                if( autowireBean == null){
                    throw new Exception("The Bean "+ qualifier.value() +" is not exist!");
                }
                field.set( instance , autowireBean.getWrappedInstance());
                continue;
            }
            //否则按照类型注入
            //先根据Type来获取对应的BeanDefinition,如果为抽象Bean(不能实例化)则使用实现类的类名来获取已经实例化的bean
            CPBeanDefinition fieldBeanDefinition =  getBeanDefinition(field.getType().getName());
            String fieldBeanName = "";
            if (fieldBeanDefinition == null ){
                //如果注入的类型是实现类,遍历所有的beanNames,找到满足BeanDefinition的Bean -- spring里不是这样写的
                String[] beanNames = getBeanNamesForType(field.getType());
                if( beanNames == null || beanNames.length >  1){
                    throw new Exception( " instance " + instance.getClass() + " field " + field.getType() + "has not unique Bean");
                }
                fieldBeanName = beanNames[0];
            }else if(fieldBeanDefinition.isAbstract()){
                //注入的为抽象接口
                fieldBeanName = fieldBeanDefinition.getBeanClassName();
            }
            field.set(instance , factoryBeanObjectCache.get(fieldBeanName));


        }

    }

    private Object instantiateBean(String beanName, CPBeanDefinition beanDefinition) {
        Object instance = null;
        try {
            //如果是单例，就尝试从单例的缓存里获取，没有则创建并添加到缓存
            if( beanDefinition.isSingleton() ){
                if( factoryBeanObjectCache.containsKey(beanName)){
                    instance = factoryBeanObjectCache.get(beanName);
                }else{
                    Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                    instance = clazz.newInstance();
                    factoryBeanObjectCache.put(beanName,instance);
                    factoryBeanObjectCache.put(beanDefinition.getBeanClassName(),instance);
                }
            }else{
                //原型bean就直接通过className创建实例
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                instance = clazz.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }


    private Object initializeBean(String beanName, Object exposedObject, CPBeanDefinition beanDefinition) {
        return exposedObject;
    }
    protected abstract  void preInstantiateSingletons() throws Exception;


    protected abstract CPBeanDefinition  getBeanDefinition(String beanName) ;

}

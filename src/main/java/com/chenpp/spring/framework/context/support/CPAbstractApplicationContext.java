package com.chenpp.spring.framework.context.support;

import com.chenpp.spring.framework.beans.CPBeanFactory;
import com.chenpp.spring.framework.beans.config.CPBeanDefinition;
import com.chenpp.spring.framework.beans.support.CPBeanDefinitionReader;
import com.chenpp.spring.framework.beans.support.CPDefaultListableBeanFactory;
import com.chenpp.spring.framework.context.CPApplicationContext;

import java.util.List;


/**
 * 2020/2/6
 * created by chenpp
 */
public abstract class CPAbstractApplicationContext implements CPApplicationContext {

    private String[] configLocations;

    private CPDefaultListableBeanFactory beanFactory;

    private CPBeanDefinitionReader reader;

    public CPAbstractApplicationContext(String... configLocations){
        this.configLocations = configLocations;
    }

    //可提供给子类重写
    public void refresh() throws Exception {

        //1.定位配置文件并加载，扫描相关的类
        //在spring里是通过工厂生成了一个DefaultListableBeanFactory实例,并赋值给成员变量beanFactory
        //参考AbstractRefreshableApplicationContext里的refreshBeanFactory
        this.beanFactory = createBeanFactory();

        reader = new CPBeanDefinitionReader(this.beanFactory,configLocations);

        //2.把它们封装成BeanDefinition
        List<CPBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

        //3.注册，把配置信息放到容器里面(伪IOC容器实际是BeanDefinition的Map)
        doRegisterBeanDefinition(beanDefinitions);

        //4.判断是否是延时加载的类，不是的话就提前进行初始化
        finishBeanFactoryInitialization();


    }

    protected void finishBeanFactoryInitialization(){
        try {
            this.beanFactory.preInstantiateSingletons();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doRegisterBeanDefinition(List<CPBeanDefinition> beanDefinitions) throws Exception {
        for (CPBeanDefinition beanDefinition: beanDefinitions) {
            if(reader.getRegistry().containsBeanDefinition(beanDefinition.getFactoryBeanName())){
                throw new Exception("The “" + beanDefinition.getFactoryBeanName() + "” has existed!!");
            }
            reader.getRegistry().registerBeanDefinition(beanDefinition.getFactoryBeanName(),beanDefinition);
        }
    }

    /***
     * spring中这个的实现类是其他的AbstractApplicationContext的子类，这里就不写的这么复杂了
     */
    @Override
    public CPBeanFactory getBeanFactory() {
        return this.beanFactory;
    }



    @Override
    public Object getBean(String name) throws Exception {
       return  getBeanFactory().getBean(name);
    }

    protected CPDefaultListableBeanFactory createBeanFactory() {
        return new CPDefaultListableBeanFactory();
    }

}

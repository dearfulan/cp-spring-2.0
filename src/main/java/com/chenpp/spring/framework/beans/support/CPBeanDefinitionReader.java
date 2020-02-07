package com.chenpp.spring.framework.beans.support;

import com.chenpp.spring.framework.annotation.CPController;
import com.chenpp.spring.framework.annotation.CPService;
import com.chenpp.spring.framework.beans.config.CPBeanDefinition;
import com.chenpp.spring.framework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 2020/2/6
 * created by chenpp
 */
public class CPBeanDefinitionReader {

    private Properties config = new Properties();

    private static String SCAN_PACKAGE = "scanPackage";

    private static List<String> registyBeanClasses = new ArrayList<>();

    private CPBeanDefinitionRegistry registry;


    public CPBeanDefinitionReader(CPBeanDefinitionRegistry registry,String... locations){
        //通过URL定位找到其所对应的文件，然后转换为文件流
        this.registry = registry;
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:",""));
        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null != is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        doScannClasses(config.getProperty(SCAN_PACKAGE));
    }

    public CPBeanDefinitionRegistry getRegistry(){
        return this.registry;
    }

    private void doScannClasses(String scanPackage) {
        //转换为文件路径，实际上就是把.替换为/就OK了  对于非Web环境需要把classLoader去掉
        URL url = this.getClass().getResource("/" + scanPackage.replaceAll("\\.","/"));
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if(file.isDirectory()){
                doScannClasses(scanPackage + "." + file.getName());
            }else{
                if(!file.getName().endsWith(".class")){ continue;}
                String className = (scanPackage + "." + file.getName().replace(".class",""));
                registyBeanClasses.add(className);
            }
        }
    }

    /**
     * 将扫描到的class转化成对应的BeanDefinition
     */
    public List<CPBeanDefinition> loadBeanDefinitions(){

        List<CPBeanDefinition> beanDefinitions = new ArrayList<>();
        try {
            for( String className : registyBeanClasses){
                Class<?> beanClass = Class.forName(className);
                if(beanClass.isInterface()) { continue; }
                //beanName有三种情况: 1)自定义名字 2)默认是类名首字母小写 3)接口注入

                //对于添加了CPController和CPService注解,判断是否有定义好的beanName
                String beanName = StringUtils.toFirstLowChar(beanClass.getSimpleName());
                if(beanClass.isAnnotationPresent(CPController.class) && !StringUtils.isEmpty(beanClass.getAnnotation(CPController.class).value()) ){
                    beanName = beanClass.getAnnotation(CPController.class).value();
                }else if(beanClass.isAnnotationPresent(CPService.class) && !StringUtils.isEmpty(beanClass.getAnnotation(CPService.class).value())){
                    beanName = beanClass.getAnnotation(CPService.class).value();
                }

                CPBeanDefinition beanDefinition = doCreateBeanDefinition(beanName,className,false);
                beanDefinitions.add(beanDefinition);
                //如果是实现类,因为我们不会在接口上加注解,所以使用接口名来定义beanName,对应的BeanDefinition还是实现类的
                Class<?> [] interfaces = beanClass.getInterfaces();
                for (Class<?> i : interfaces) {
                    //如果是多个实现类，只能覆盖
                    //这个时候，可以自定义名字
                    beanDefinitions.add(doCreateBeanDefinition(i.getName(),beanClass.getName(),true));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return beanDefinitions;
    }


    //把每一个配信息解析成一个BeanDefinition
    private CPBeanDefinition doCreateBeanDefinition(String factoryBeanName,String beanClassName , boolean isAbstract){
        CPBeanDefinition beanDefinition = new CPBeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        beanDefinition.setAbstract(isAbstract);
        return beanDefinition;
    }



}

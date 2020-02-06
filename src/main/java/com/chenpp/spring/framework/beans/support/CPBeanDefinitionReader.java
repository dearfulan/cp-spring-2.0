package com.chenpp.spring.framework.beans.support;

import com.chenpp.spring.framework.beans.config.CPBeanDefinition;

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
        //转换为文件路径，实际上就是把.替换为/就OK了
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.","/"));
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
                Class clazz = Class.forName(className);
                if(clazz.isInterface()){continue;}
                CPBeanDefinition beanDefinition = doCreateBeanDefinition(toLowerFirstCase(clazz.getSimpleName()),className);
                if(beanDefinition != null){
                    beanDefinitions.add(beanDefinition);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return beanDefinitions;
    }


    //把每一个配信息解析成一个BeanDefinition
    private CPBeanDefinition doCreateBeanDefinition(String factoryBeanName,String beanClassName){
        CPBeanDefinition beanDefinition = new CPBeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }


    private String toLowerFirstCase(String simpleName) {
        char [] chars = simpleName.toCharArray();
        //大小写字母的ASCII码相差32, 而且大写字母的ASCII码要小于小写字母的ASCII码
        chars[0] += 32;
        return String.valueOf(chars);
    }
}

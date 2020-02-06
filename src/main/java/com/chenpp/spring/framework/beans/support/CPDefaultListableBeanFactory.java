package com.chenpp.spring.framework.beans.support;

import com.chenpp.spring.framework.beans.config.CPBeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 2020/2/6
 * created by chenpp
 */
public class CPDefaultListableBeanFactory extends CPAbstractBeanFactory implements  CPBeanDefinitionRegistry {


    //存储注册信息的BeanDefinition
    private final Map<String, CPBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);


    @Override
    public void preInstantiateSingletons() throws Exception {
        for (Map.Entry<String, CPBeanDefinition> beanDefinitionEntry : beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if(!beanDefinitionEntry.getValue().isLazyInit()) {
                try {
                    getBean(beanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void registerBeanDefinition(String beanName, CPBeanDefinition beanDefinition) throws Exception {
        beanDefinitionMap.put(beanName,beanDefinition);
    }

    @Override
    public void removeBeanDefinition(String beanName) throws Exception {
        beanDefinitionMap.remove(beanName);
    }

    @Override
    public CPBeanDefinition getBeanDefinition(String beanName) throws Exception {
        return  beanDefinitionMap.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }
}

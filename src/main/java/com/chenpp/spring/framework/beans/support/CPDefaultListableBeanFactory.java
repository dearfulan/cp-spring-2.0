package com.chenpp.spring.framework.beans.support;

import com.chenpp.spring.framework.beans.config.CPBeanDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 2020/2/6
 * created by chenpp
 */
public class CPDefaultListableBeanFactory extends CPAbstractBeanFactory implements CPListableBeanFactory,  CPBeanDefinitionRegistry {


    //存储注册信息的BeanDefinition 伪IOC容器
    private final Map<String, CPBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    private volatile List<String> beanDefinitionNames = new ArrayList<>(256);

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
        this.beanDefinitionNames.add(beanName);

    }

    @Override
    public <T> T  getBean(Class<?> beanClass) throws Exception {
        String[] beanNames = getBeanNamesForType(beanClass);
        if(beanNames == null){
            throw new Exception("No Bean Definition : "+beanClass);
        }
        if(beanNames != null && beanNames.length == 1){
            return (T)getBean(beanNames[0]);
        }
        if(beanNames.length > 1){
            throw new Exception("No Unique Bean Definition : "+beanClass);
        }
        return null;
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionNames.toArray(new String[beanDefinitionNames.size()]);
    }


    @Override
    public CPBeanDefinition getBeanDefinition(String beanName)  {
        return  beanDefinitionMap.get(beanName);
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        List<String> result = new ArrayList<>();
        // Check all bean definitions.
        for (String beanName : this.beanDefinitionNames) {
            CPBeanDefinition beanDefinition = getBeanDefinition(beanName);
            if( beanDefinition != null && !beanDefinition.isAbstract() && type.getName().equals( beanDefinition.getBeanClassName()) ){
                result.add(beanName);
            }
        }
        return result.toArray(new String[result.size()]) ;
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }
}

package com.chenpp.spring.framework.context.support;



/**
 * 2020/2/6
 * created by chenpp
 */
public class CPClassPathXmlApplicationContext extends  CPAbstractApplicationContext{

    public CPClassPathXmlApplicationContext( String[] configLocations) {
        super(configLocations);
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

package com.chenpp.spring.test;

import com.chenpp.spring.demo.controller.MyController;
import com.chenpp.spring.demo.service.IModifyService;
import com.chenpp.spring.demo.service.IQueryService;
import com.chenpp.spring.demo.service.impl.ModifyServiceImpl;
import com.chenpp.spring.demo.service.impl.QueryServiceImpl;
import com.chenpp.spring.framework.context.support.CPClassPathXmlApplicationContext;

import javax.sound.midi.SoundbankResource;
import javax.xml.bind.SchemaOutputResolver;

/**
 * 2020/2/7
 * created by chenpp
 */
public class SpringTest {
    public static void main(String[] args) {
        CPClassPathXmlApplicationContext context = new CPClassPathXmlApplicationContext("classpath:application.properties");
        System.out.println(context);
        try {
            MyController myController = (MyController) context.getBean("myController");
            System.out.println(myController);
            IModifyService modifyService = (ModifyServiceImpl) context.getBean("modifyService");
            System.out.println(modifyService);

            System.out.println(modifyService.edit(1,"chnm"));
            IQueryService queryService = (QueryServiceImpl) context.getBean(QueryServiceImpl.class);
            System.out.println(queryService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

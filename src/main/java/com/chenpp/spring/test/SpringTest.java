package com.chenpp.spring.test;

import com.chenpp.spring.demo.controller.MyController;
import com.chenpp.spring.demo.service.IModifyService;
import com.chenpp.spring.demo.service.IQueryService;
import com.chenpp.spring.demo.service.impl.MyServiceImpl;
import com.chenpp.spring.demo.service.impl.QueryServiceImpl;
import com.chenpp.spring.framework.context.support.CPClassPathXmlApplicationContext;

import javax.sound.midi.SoundbankResource;


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
            MyServiceImpl myService = (MyServiceImpl) context.getBean("myService");
            System.out.println(myService.test("cglib"));
            System.out.println("=========================================");
            IModifyService modifyService = (IModifyService) context.getBean("modifyService");

            System.out.println(modifyService.add("chenp","beijing"));
            System.out.println("=========================================");
            IQueryService queryService = (IQueryService) context.getBean(QueryServiceImpl.class);
            queryService.query("chenpp");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

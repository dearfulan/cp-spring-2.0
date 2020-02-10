package com.chenpp.spring.demo.service.impl;

import com.chenpp.spring.framework.annotation.CPService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 2020/2/10
 * created by chenpp
 */
@CPService("myService")
public class MyServiceImpl {

    private Logger log = LoggerFactory.getLogger(MyServiceImpl.class);

    /**
     * 查询
     */
    public String test(String name) {
        log.info("这是test的方法：" + name);
        return name;
    }
}

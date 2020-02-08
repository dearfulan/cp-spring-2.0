package com.chenpp.spring.demo.controller;

import com.chenpp.spring.framework.annotation.*;
import com.chenpp.spring.framework.web.servlet.CPModel;
import com.chenpp.spring.framework.web.servlet.CPModelMap;


/**
 * 接口url
 * created by chenpp
 *
 */
@CPController
public class PageController {

	@CPRequestMapping("/show")
	public CPModel query( @CPRequestParam("name") String name){
		CPModel model = new CPModelMap();
		model.addAttribute("name",name);
		return model;
	}

	
}

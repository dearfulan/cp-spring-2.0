package com.chenpp.spring.demo.controller;

import com.chenpp.spring.demo.service.IModifyService;
import com.chenpp.spring.demo.service.IQueryService;
import com.chenpp.spring.framework.annotation.*;
import com.chenpp.spring.framework.web.servlet.CPModel;
import com.chenpp.spring.framework.web.servlet.CPModelAndView;
import com.chenpp.spring.framework.web.servlet.CPModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 接口url
 * created by chenpp
 *
 */
@CPController
@CPRequestMapping("/web")
public class MyController {

	@CPAutowire
	IQueryService queryService;
	@CPAutowire
	IModifyService modifyService;

	@CPRequestMapping("/query.json")
	public String query(CPModel model, HttpServletRequest request, HttpServletResponse response, @CPRequestParam("name") String name){
		String result = queryService.query(name);
		model.addAttribute("name",name);
		return "show";
	}
	
	@CPRequestMapping("/add*.json")
	public CPModelAndView add(HttpServletRequest request, HttpServletResponse response,
							  @CPRequestParam("name") String name, @CPRequestParam("addr") String addr) throws Exception {
		modifyService.add(name,addr);
		CPModelAndView modelAndView = new CPModelAndView();
		modelAndView.setViewName("add.html");
		modelAndView.addObject("name",name);
		modelAndView.addObject("addr",addr);

		return modelAndView;
	}
	
	@CPRequestMapping("/remove.json")
	@CPResponseBody
	public String remove(HttpServletRequest request,HttpServletResponse response,
		   @CPRequestParam("id") Integer id){
		String result = modifyService.remove(id);
		return result;
	}
	
	@CPRequestMapping("/edit.json")
	public void edit(HttpServletRequest request,HttpServletResponse response, @CPRequestParam("id") Integer id, @CPRequestParam("name") String name){
		String result = modifyService.edit(id,name);
		out(response,result);
	}
	
	
	
	private void out(HttpServletResponse resp,String str){
		try {
			resp.getWriter().write(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

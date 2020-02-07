package com.chenpp.spring.demo.controller;

import com.chenpp.spring.demo.service.IModifyService;
import com.chenpp.spring.demo.service.IQueryService;
import com.chenpp.spring.framework.annotation.CPAutowire;
import com.chenpp.spring.framework.annotation.CPController;
import com.chenpp.spring.framework.annotation.CPRequestMapping;
import com.chenpp.spring.framework.annotation.CPRequestParam;
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
	public void query(HttpServletRequest request, HttpServletResponse response, @CPRequestParam("name") String name){
		String result = queryService.query(name);
		out(response,result);
	}
	
	@CPRequestMapping("/add*.json")
	public void add(HttpServletRequest request,HttpServletResponse response,
			   @CPRequestParam("name") String name,@CPRequestParam("addr") String addr){
		String result = modifyService.add(name,addr);
		out(response,result);
	}
	
	@CPRequestMapping("/remove.json")
	public void remove(HttpServletRequest request,HttpServletResponse response,
		   @CPRequestParam("id") Integer id){
		String result = modifyService.remove(id);
		out(response,result);
	}
	
	@CPRequestMapping("/edit.json")
	public void edit(HttpServletRequest request,HttpServletResponse response,
			@CPRequestParam("id") Integer id,
			@CPRequestParam("name") String name){
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

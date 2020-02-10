package com.chenpp.spring.demo.service.impl;

import com.chenpp.spring.demo.service.IModifyService;
import com.chenpp.spring.demo.service.IQueryService;
import com.chenpp.spring.framework.annotation.CPAutowire;
import com.chenpp.spring.framework.annotation.CPService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 增删改业务
 * created by chenpp
 *
 */
@CPService("modifyService")
public class ModifyServiceImpl implements IModifyService {

	private Logger log = LoggerFactory.getLogger(ModifyServiceImpl.class);

//	@CPAutowire
//	private IQueryService queryService;

	/**
	 * 增加
	 */
	public String add(String name,String addr) throws Exception{
		if( name == null){
			throw new Exception("这是故意抛出的异常");
		}
		//queryService.query(name);
		log.info("这是在执行modify的add方法....");
		return "modifyService add,name=" + name + ",addr=" + addr;
	}

	/**
	 * 修改
	 */
	public String edit(Integer id,String name) {
		System.out.println("这是在执行edit方法.....");
		return "modifyService edit,id=" + id + ",name=" + name;
	}

	/**
	 * 删除
	 */
	public String remove(Integer id) {
		return "modifyService delete , id=" + id;
	}
	
}

package com.chenpp.spring.demo.service.impl;

import com.chenpp.spring.demo.service.IQueryService;
import com.chenpp.spring.framework.annotation.CPService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 查询业务
 * created by chenpp
 *
 */
@CPService
public class QueryServiceImpl implements IQueryService {

	private Logger log = LoggerFactory.getLogger(QueryServiceImpl.class);

	/**
	 * 查询
	 */
	public String query(String name) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		String json = "{name:\"" + name + "\",time:\"" + time + "\"}";
		log.info("这是在业务方法中打印的：" + json);
		return json;
	}

}

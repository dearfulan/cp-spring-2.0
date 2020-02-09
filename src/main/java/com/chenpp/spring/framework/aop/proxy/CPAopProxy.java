

package com.chenpp.spring.framework.aop.proxy;



/**
 * 2020/2/9
 * created by chenpp
 */
public interface CPAopProxy {

	/**
	 * 创建代理对象
	 */
	Object getProxy();

	/**
	 * Create a new proxy object.
	 * @return the new proxy object
	 */
	Object getProxy( ClassLoader classLoader);

}

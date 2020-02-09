/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chenpp.spring.framework.aop.intercept;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 切面里用于保存代理对象相关信息的类
 */
public interface CPJoinpoint {


	Object proceed() throws Throwable;

	//返回目标源对象target
	Object getThis();

	Object[] getArguments();

	Method getMethod();

	void setUserAttribute(String key, Object value);

	Object getUserAttribute(String key);

}

package com.chenpp.spring.framework.web.servlet;


import com.chenpp.spring.framework.annotation.CPRequestMapping;
import com.chenpp.spring.framework.context.CPApplicationContext;
import com.chenpp.spring.framework.context.support.CPClassPathXmlApplicationContext;
import com.chenpp.spring.framework.util.StringUtils;
import com.chenpp.spring.framework.web.servlet.mvc.CPRequestMappingHandlerAdapter;
import com.chenpp.spring.framework.web.servlet.view.CPInternalResourceViewResolver;
import com.chenpp.spring.framework.web.servlet.view.CPJsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CPDispatcherServlet extends HttpServlet{

	private Logger logger = LoggerFactory.getLogger(CPDispatcherServlet.class);

	private CPApplicationContext applicationContext;

	private final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

	private final String PREFIX  = "prefix";

	private final String SUFFIX = "suffix";

	private List<CPHandlerMapping> handlerMappings = new ArrayList<>();

	private List<CPHandlerAdapter> handlerAdapters = new ArrayList<>();

	private List<CPViewResolver> viewResolvers = new ArrayList<>();


	//如果返回的是ResponseBody,返回一个默认的viewName,区分于那些没有设置viewName的
	private final static String DEFAULT_VIEW_NAME = ".DEFAULT_VIEW";




	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		CPApplicationContext context = new CPClassPathXmlApplicationContext(servletConfig.getInitParameter(CONTEXT_CONFIG_LOCATION));
		this.applicationContext = context;
		onRefresh(context);
		logger.info("spring 容器启动成功.....");
	}


	protected void onRefresh(CPApplicationContext context) {
		initStrategies(context);
	}

	//初始化策略
	protected void initStrategies(CPApplicationContext context) {
		//多文件上传的组件
		initMultipartResolver(context);
		//初始化本地语言环境
		initLocaleResolver(context);
		//初始化模板处理器
		initThemeResolver(context);
		//handlerMapping,必须初始化
		initHandlerMappings(context);
		//初始化参数适配器,必须初始化
		initHandlerAdapters(context);
		//初始化异常拦截器
		initHandlerExceptionResolvers(context);
		//初始化视图预处理器
		initRequestToViewNameTranslator(context);
		//初始化视图转换器,必须初始化
		initViewResolvers(context);
		//初始化FlashMap管理器  FlashMap:用于保存重定向使用的数据
		initFlashMapManager(context);
	}
	private void initFlashMapManager(CPApplicationContext context) {
	}

	private void initViewResolvers(CPApplicationContext context) {
		//初始化视图解析器,这里简化,只需要初始化InternalResourceViewResolver就可以了
		Properties config = context.getConfig();
		this.viewResolvers.add(new CPInternalResourceViewResolver(config.getProperty(PREFIX),config.getProperty(SUFFIX)));
	}

	private void initRequestToViewNameTranslator(CPApplicationContext context) {
	}

	private void initHandlerExceptionResolvers(CPApplicationContext context) {
	}

    //spring中HandlerAdapter的初始化思路和HandlerMapping类似,这里直接实例化一个默认的就好
	private void initHandlerAdapters(CPApplicationContext context) {
		//HandlerAdapter用于把request的参数 适配成和Handler匹配的参数,这里只需要一个HandlerAdapter来匹配数据就好
		this.handlerAdapters.add(new CPRequestMappingHandlerAdapter());

	}

	// spring中是采用策略模式,不同场景下使用不同的HandlerMapping
	// Spring里默认情形下是没有缓存url和Method对应的关系,只缓存了url和Controller
	// Spring里注册Handler的映射关系是在 AbstractDetectingUrlHandlerMapping的initApplicationContext
	// 该类也是Spring默认的BeanNameUrlHandlerMapping的父类

	// 对于DefaultListableBeanFactory.getBeansOfType会得到一个beanName和HandlerMapping的映射集合
	// --猜测这是处理用户自定义handlerMapping的代码
	private void initHandlerMappings(CPApplicationContext context) {
		String[] beanNames  = context.getBeanFactory().getBeanDefinitionNames();
        try{
			for(String beanName:beanNames){
				Object instance = context.getBean(beanName);
				if(instance == null ) { continue; }

				Class<?> clazz = instance.getClass();
				//判断Controller类上是否有CPRequestMapping注解
				String baseUrl = "";
				if(clazz.isAnnotationPresent(CPRequestMapping.class)) {
					baseUrl = clazz.getAnnotation(CPRequestMapping.class).value();
				}
				Method[] methods = clazz.getDeclaredMethods();
				//遍历CPController上的Method 获取url与MethodMapping的映射关系
				for(Method method:methods){
					if(! method.isAnnotationPresent(CPRequestMapping.class)){ continue; }
					String methodUrl = method.getAnnotation(CPRequestMapping.class).value();
					String regex =  ("/" + baseUrl + "/" + methodUrl ).replaceAll("/+", "/");
					Pattern pattern = Pattern.compile(regex);
					CPHandlerMapping handlerMapping = new CPHandlerMapping(pattern,method,instance);
					handlerMappings.add(handlerMapping);
				}
			}
		}catch (Exception e){
        	e.printStackTrace();
		}

	}

	private void initThemeResolver(CPApplicationContext context) {
	}

	private void initLocaleResolver(CPApplicationContext context) {
	}

	private void initMultipartResolver(CPApplicationContext context) {
	}


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try{
			this.doDispatcher(req, resp);
		}catch (Exception e){
			resp.getWriter().write("500 Exception,Details:\r\n" + Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]", "").replaceAll(",\\s", "\r\n"));

		}

	}

	private void doDispatcher(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Exception ex = null ;
		CPHandlerMapping handler = null ;
		CPModelAndView mv = null ;
		try{
			//1.根据url找到对应的Handler(spring返回的是一个HandlerExecutionChain,包含handler和interceptors)
			handler = getHandler(req);
			if( handler == null){
				//没有handler,这里我直接返回404
				processDispatchResult(req,resp,new CPModelAndView("404"),null);
				return ;
			}
			//2.根据Handler找到对应的HandlerAdapter
			CPHandlerAdapter handlerAdapter = getHandlerAdapter(handler);

			//3.使用HandlerAdapter实际处理请求,返回结果视图对象  在这里会执行业务方法
			mv = handlerAdapter.handle(req, resp, handler);

			//4.找到默认的视图
			// 如果没有添加ResponseBody注解，也没有返回ViewName,spring会通过RequestToViewNameTranslator从request找到请求的url作为viewName
			applyDefaultViewName(req, mv);

		}catch (Exception e){
			ex = e;
		}
		// 5. 处理ModeAndView视图,进行渲染
		processDispatchResult( req, resp ,mv, ex);

	}

	private void applyDefaultViewName(HttpServletRequest req, CPModelAndView mv) {
		if (mv != null && StringUtils.isEmpty(mv.getViewName())) {
		    String url = req.getRequestURI();
			String contextPath = req.getContextPath();
			url = url.replace(contextPath, "").replaceAll("/+", "/");
			String defaultViewName = url;
			if (defaultViewName != null) {
				mv.setViewName(defaultViewName);
			}
		}
	}

	private CPHandlerMapping getHandler(HttpServletRequest req) {
		String url = req.getRequestURI();
		String contextPath = req.getContextPath();
		url = url.replace(contextPath, "").replaceAll("/+", "/");
		//遍历HandlerMappings,获得和url匹配的HandlerMapping
		CPHandlerMapping handlerMapping = null;
		for(CPHandlerMapping hm : handlerMappings){
			if( hm.getPattern().matcher(url).matches()){
				handlerMapping = hm;
			}
		}
		return handlerMapping;
	}

	private CPHandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
		if (this.handlerAdapters != null) {
			for (CPHandlerAdapter ha : this.handlerAdapters) {
				if (ha.supports(handler)) {
					return ha;
				}
			}
		}
		throw new ServletException("No adapter for handler [" + handler +
				"]: The DispatcherServlet configuration needs to include a HandlerAdapter that supports this handler");
	}

	private void processDispatchResult(HttpServletRequest request, HttpServletResponse response, CPModelAndView mv, Exception exception) throws Exception {
		  //如果有异常,则返回500页面
		  if (exception != null){
		  	 CPModelAndView modelAndView = new CPModelAndView("500");
		  	 modelAndView.addObject("message",exception.getCause());
		  	 modelAndView.addObject("stackTrace",Arrays.toString(exception.getStackTrace()).replaceAll("\\[|\\]",""));
		  	 processDispatchResult(request,response,modelAndView,null);
		  }
		  if( mv == null) return ;
		  String viewName = mv.getViewName();
		  if (!StringUtils.isEmpty(viewName)) {
			  CPView view ;
			  if(viewName.equals(DEFAULT_VIEW_NAME)){
				  view = new CPJsonView();
			  }else{
				  // 根据 viewName 使用 viewResolver得到View
				  view  = resolveViewName(viewName);
			  }
			  if (view == null) {
				  throw new ServletException("Could not resolve view with name '" + mv.getViewName() + "' in servlet with name '" + getServletName() + "'");
			  }
			  //渲染数据
			  view.render(mv.getModelMap(),request,response);
		  }
	}

	protected CPView resolveViewName(String viewName) throws Exception {
		if (this.viewResolvers != null) {
			for (CPViewResolver viewResolver : this.viewResolvers) {
				//根据viewName和对应的前后缀,找到对应的View文件路径
				CPView view = viewResolver.resolveViewName(viewName, null);
				if (view != null) {
					return view;
				}
			}
		}
		return null;
	}



	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		this.doPost(req,resp);
	}
}

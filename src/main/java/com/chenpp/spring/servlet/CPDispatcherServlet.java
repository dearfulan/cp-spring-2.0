package com.chenpp.spring.servlet;


import java.io.IOException;
import java.util.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CPDispatcherServlet extends HttpServlet{


    @Override
	public void init(ServletConfig servletConfig) throws ServletException {

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

	}



	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		this.doPost(req,resp);
	}
}

package com.chenpp.spring.framework.web.servlet.view;

import com.chenpp.spring.framework.util.Constants;
import com.chenpp.spring.framework.web.servlet.CPDispatcherServlet;
import com.chenpp.spring.framework.web.servlet.CPView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 2020/2/8
 * created by chenpp
 */
public class CPJsonView implements CPView {

    public CPJsonView(){}


    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter printWriter = response.getWriter();
        printWriter.println(model.get(Constants.RESPONSE_NODY));
        printWriter.flush();
        printWriter.close();
    }
}

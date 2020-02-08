package com.chenpp.spring.framework.web.servlet.view;

import com.chenpp.spring.framework.util.StringUtils;
import com.chenpp.spring.framework.web.servlet.CPView;
import com.chenpp.spring.framework.web.servlet.CPViewResolver;
import java.util.Locale;

/**
 * 2020/2/8
 * created by chenpp
 */
public class CPInternalResourceViewResolver implements CPViewResolver {

    private String prefix ;
    private String suffix ;


    public CPInternalResourceViewResolver(String prefix,String suffix){
        this.prefix = this.getClass().getClassLoader().getResource(prefix).getFile(); ;
        this.suffix = suffix ;
    }

    @Override
    public CPView resolveViewName(String viewName, Locale locale) throws Exception {
        if( StringUtils.isEmpty(viewName) ){
            return null ;
        }
        viewName = viewName.endsWith(suffix) ? viewName : (viewName + suffix);
        String url = ( prefix + "/" + viewName ).replaceAll("/+","/");
        return new CPHtmlView(url);
    }

}

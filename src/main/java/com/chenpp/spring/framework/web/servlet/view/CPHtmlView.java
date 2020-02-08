package com.chenpp.spring.framework.web.servlet.view;

import com.chenpp.spring.framework.web.servlet.CPView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 2020/2/8
 * created by chenpp
 */
public class CPHtmlView  implements CPView {

    private String url;


    public CPHtmlView(String url){
        this.url = url;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        StringBuffer sb = new StringBuffer();
        //RandomAccessFile是专门处理文件的类
        RandomAccessFile ra = new RandomAccessFile(new File(url),"r");
        String line  = null;
        while (null != (line = ra.readLine())){
            line = new String(line.getBytes("ISO-8859-1"),"utf-8");
            //读取文件后,进行正则匹配,匹配的标签为￥{XX}
            Pattern pattern = Pattern.compile("￥\\{[^\\}]+\\}",Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()){
                String paramName = matcher.group();
                //替换掉￥{},这样就可以得到需要传到页面的属性名attributeName
                paramName = paramName.replaceAll("￥\\{|\\}","");
                //根据属性名从model里获得属性值
                Object paramValue = model.get(paramName);
                if(null == paramValue){ continue;}
                //如果有属性值,则替换掉标签
                line = matcher.replaceFirst(makeStringForRegExp(paramValue.toString()));
                matcher = pattern.matcher(line);
            }
            sb.append(line);
        }

        response.setCharacterEncoding("utf-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(sb.toString());
        printWriter.flush();
        printWriter.close();
    }

    //处理特殊字符
    public static String makeStringForRegExp(String str) {
        return str.replace("\\", "\\\\").replace("*", "\\*")
                .replace("+", "\\+").replace("|", "\\|")
                .replace("{", "\\{").replace("}", "\\}")
                .replace("(", "\\(").replace(")", "\\)")
                .replace("^", "\\^").replace("$", "\\$")
                .replace("[", "\\[").replace("]", "\\]")
                .replace("?", "\\?").replace(",", "\\,")
                .replace(".", "\\.").replace("&", "\\&");
    }
}

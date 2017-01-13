package com.wly.controller;

import com.wly.utils._HttpConnection;
import com.wly.utils._HttpConnection.HttpMethod;
import com.wly.utils._HttpConnection.HttpType;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BaseController {
    private Logger log = Logger.getLogger(BaseController.class);
    protected HttpServletRequest request = ServletActionContext.getRequest();
    protected HttpServletResponse response = ServletActionContext.getResponse();
    protected HttpSession session = request.getSession();
    protected static final Integer pagesize = 20;

    protected String getContextParameter(String param) {
        return request.getSession().getServletContext().getInitParameter(param);
    }

    /**
     * 获取转发根地址
     *
     * @return
     */
    protected String getServer() {
        String server = "";
        Properties prop = new Properties();
        try {
            prop.load(this.getClass().getClassLoader().getResourceAsStream("/config.properties"));
            server = "http://" + prop.get("host") + ":" + prop.get("port") + "/" + prop.get("path") + "/";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return server;
    }


    /**
     * 报文转发
     *
     * @param
     * @return
     */
    protected String sendRequest(String uri) {
        this.printLog(log);
        _HttpConnection conn = new _HttpConnection(HttpType.http, HttpMethod.POST);
        try {
            return conn.sendRequest(uri, this.modarg());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 封装参数
     *
     * @return
     */
    protected Map<String, Object> modarg() {
        Map<String, Object> map = new HashMap<String, Object>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            try {
                map.put(paramName, URLEncoder.encode(
                        new String(request.getParameter(paramName).getBytes("iso-8859-1"), "utf-8"), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 获取请求的参数并转换为相应的实体类
     *
     * @param clazz 类 类型
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    @SuppressWarnings("unchecked")
    protected <T> T getBeanByRequest(Class<T> clazz) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Enumeration<String> paramNames = request.getParameterNames();
        Method[] methods = clazz.getDeclaredMethods();
        T obj = clazz.newInstance();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            paramName = paramName.toUpperCase().charAt(0) + paramName.substring(1);
            for (Method method : methods) {
                if (method.getName().startsWith("set")) {
                    if (method.getName().endsWith(paramName)) {
                        if (request.getParameter(paramName.toLowerCase()) != null) {
                            String other = request.getParameter(paramName.toLowerCase());
                            Type type = method.getGenericParameterTypes()[0];
                            if (type.equals(Integer.class) || type.equals(int.class)) {
                                method.invoke(obj, new Object[]{Integer.parseInt(other)});
                            } else if (type.equals(Double.class)) {
                                method.invoke(obj, new Object[]{Double.parseDouble(other)});
                            } else if (type.equals(BigDecimal.class)) {
                                method.invoke(obj, new Object[]{BigDecimal.valueOf(Double.parseDouble(other))});
                            } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
                                method.invoke(obj, new Object[]{Boolean.valueOf(other)});
                            } else {
                                method.invoke(obj, new Object[]{other});
                            }

                        }
                    }
                }
            }
        }
        return obj;
    }

    /**
     * 打印参数
     */
    protected void printLog(Logger log) {
        try {
            Enumeration<String> enums = request.getParameterNames();
            while (enums.hasMoreElements()) {
                String parne = enums.nextElement();
                log.debug("result: " + parne + "=" + new String(request.getParameter(parne).getBytes("iso-8859-1"), "utf-8"));
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 输出json数据
     *
     * @param json
     * @throws IOException
     */
    protected void output(String json) {
        try {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter output = response.getWriter();
            if ("true".equals(request.getParameter("jsonp")))
                output.print(request.getParameter("callback") + "(" + json + ")");
            else
                output.print(json);
            output.flush();
            output.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 获取分页需要的和页大小
     *
     * @throws IOException
     */
    protected Map<String, String> getPageInfo() {
        Map map = new HashMap();
        try {
            Integer pageindex = Integer.parseInt((null == request.getParameter("pageindex") || "".equals(request.getParameter("pageindex")) ? "1" : request.getParameter("pageindex")));
            map.put("pageindex", pageindex + "");
            map.put("pagesize", pagesize + "");
            map.put("rowstart", (pageindex - 1) * pagesize + "");
            map.put("rowend", pagesize + "");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return map;
    }
}

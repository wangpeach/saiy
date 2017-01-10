package com.wly.controller;

import com.google.gson.Gson;
import com.wly.service.CqsscService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

@Controller
@Scope(value = "prototype")
public class CqsscController extends BaseController {

    private int limit;

    private String day;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }


    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    ServletContext context = request.getServletContext();

    @Resource
    private CqsscService cqsscService;

    public String synchronize() {
        String day = request.getParameter("day");
        return null;
    }

    /**
     * 获取最新数据
     * @return
     */
    public synchronized String haoma() {
        String result = "";
        String term = request.getParameter("term");
        Object curTerm = context.getAttribute("curterm");
        Gson gson = new Gson();
        // 没有缓存最新数据则缓存它
        if(curTerm == null) {
            Map<String, Object> code = cqsscService.getLastTerm();
            result = gson.toJson(code);
            context.setAttribute("curterm", result);
        } else {
            // 最新数据已经缓存则对比是否与当前期一致，如果一致则同步最新数据，不一致说明是最新的一期数据，直接返回
            Map<String, String> map = gson.fromJson(curTerm.toString(), Map.class);
            if(!map.get("expect").endsWith(term)) {
                Map<String, Object> code = cqsscService.getLastTerm();
                if(!code.get("expect").toString().endsWith(term)) {
                    result = gson.toJson(code);
                    context.setAttribute("curterm", result);
                } else {
                    map = new HashMap<String, String>();
                    map.put("warning", "数据暂未同步");
                    result = gson.toJson(map, Map.class);
                }
            } else {
                result = gson.toJson(map);
            }
        }
        output(result);
        return null;
    }

    public String all() {
        String day = request.getParameter("date");
        output(cqsscService.synchronize(day));
        return null;
    }

    public static void main(String[] args) {
        CqsscService cs = new CqsscService();
//        cs.holdCodes("2016-12-23");
//        System.out.println(cs.synchronize("2016-12-19"));
    }
}

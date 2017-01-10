package com.wly.controller;

import com.google.gson.Gson;
import com.wly.service.CqsscService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
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
        Object curTerm = request.getServletContext().getAttribute("curterm");
        if(curTerm == null) {

        } else {
            Gson gson = new Gson();
            Map<String, String> map = gson.fromJson(curTerm.toString(), Map.class);
            if(!map.get("expect").endsWith(term)) {

            } else {
                map = new HashMap<String, String>();
                map.put("warning", "数据暂未同步");
                result = gson.toJson(map, Map.class);
            }
        }
        //获取文件第一条数据
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

package com.wly.controller;

import com.wly.service.CqsscService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Calendar;

@Controller
@Scope(value = "prototype")
public class CqsscController extends BaseController{

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

    public String haoma() {
        System.out.println(request.getParameter("limit"));
        output(cqsscService.reqHaoMa(limit, day));
        return null;
    }

    public String all() {
        Calendar cal = Calendar.getInstance();
        String cur = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
        output(cqsscService.synchronize(cur));
        return null;
    }

    public static void main(String[] args) {
        CqsscService cs = new CqsscService();
        System.out.println(cs.synchronize("2016-12-19"));
    }
}

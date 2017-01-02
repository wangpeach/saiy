package com.wly.controller;

import com.wly.service.CqsscService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

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

    public String haoma() {
//        String code = cqsscService.reqHaoMa(limit, day);
        //获取文件第一条数据
//        output(code);
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

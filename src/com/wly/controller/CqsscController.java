package com.wly.controller;

import com.google.gson.Gson;
import com.wly.service.CqsscService;
import com.wly.utils.Utils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.text.ParseException;
import java.util.Calendar;
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
        //客户端索引从0开始，所以在这里+1才是正确的期号
        int term = Integer.parseInt(request.getParameter("term")) + 1;
        if(term == 1) {
            // 更新近7天各形态数据统计
            context.setAttribute("cencusLast7", cqsscService.cencusLast7());
        }
        Object curTerm = context.getAttribute("curterm");
        Gson gson = new Gson();
        // 没有缓存最新数据则缓存它
        if(curTerm == null) {
            Map<String, Object> code = cqsscService.getLastTerm();
            context.setAttribute("curterm", gson.toJson(code));
        } else {
            // 最新数据已经缓存则对比是否与当前期一致，如果一致则同步最新数据，不一致说明是最新的一期数据，直接返回
            Map<String, String> map = gson.fromJson(curTerm.toString(), Map.class);
            String _expect = map.get("expect");
            int expect = Integer.parseInt(_expect.substring(_expect.length() - 3));
            if(expect < term) {
                Map<String, Object> code = cqsscService.getLastTerm();
                _expect = code.get("expect").toString();
                expect = Integer.parseInt(_expect.substring(_expect.length() - 3));
                if(expect == term) {
                    result = gson.toJson(code);
                    context.setAttribute("curterm", result);
                } else {
                    map = new HashMap<String, String>();
                    map.put("warning", "no syn");
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
        if(context.getAttribute("cencusLast7") == null) {
            context.setAttribute("cencusLast7", cqsscService.cencusLast7());
        }
        String day = request.getParameter("date");
        output(cqsscService.synchronize(day));
        return null;
    }

    public String cencusLast7() {
        output(context.getAttribute("cencusLast7").toString());
        return null;
    }

    public String store() {
        String stop = request.getParameter("stop");
        String rd = request.getParameter("rd");
        try {
            if(Utils.isNotNullOrEmpty(stop)) {
                Calendar stopcal = Calendar.getInstance();
                stopcal.setTime(cqsscService.dateFormat.parse(stop));

                Calendar curday = Calendar.getInstance();

                if(stopcal.before(curday)) {

                    int day = Integer.parseInt(((curday.getTime().getTime() - stopcal.getTime().getTime()) / (24*60*60*1000)) + "");

                    for (int i = 0; i < day; i++) {

                        curday.add(Calendar.DAY_OF_MONTH, -1);
                        String forday = cqsscService.dateFormat.format(curday.getTime());
                        cqsscService.holdCodes(forday);
                    }
                }
            }
            if(Utils.isNotNullOrEmpty(rd)) {
                cqsscService.holdCodes(rd);
                context.setAttribute("cencusLast7", cqsscService.cencusLast7());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        output("保存完成...");
        return null;
    }

    public static void main(String[] args) {
        CqsscService cs = new CqsscService();
        cs.holdCodes("2016-12-23");
        System.out.println(cs.synchronize("2016-12-19"));
    }
}

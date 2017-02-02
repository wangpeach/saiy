package com.wly.scheduler;

import com.wly.service.CqsscService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Calendar;

@Component
@Service(value = "historyScheduler")
public class HistoryScheduler {

    @Resource
    private CqsscService cqsscService;

    private Calendar cal = Calendar.getInstance();

    public void exec() {
        try {
            //在某一时间段暂停开奖
            Calendar cal = Calendar.getInstance();

            Calendar stopday = Calendar.getInstance();

            stopday.setTime(cqsscService.dateFormat.parse(cqsscService.properties().getProperty("stopday")));

            Calendar startday = Calendar.getInstance();
            startday.setTime(cqsscService.dateFormat.parse(cqsscService.properties().getProperty("startday")));

            if(cal.before(stopday) || cal.after(startday)) {
                System.out.println("开始调度..");
                boolean stop = false;
                int i = 1;
                while (!stop) {
                    System.out.println("尝试第"+ i +"次获取...");
                    String code = cqsscService.reqHaoMa(1, null);
                    if(code != null) {
                        stop = cqsscService.putCode(code);
                    } else {
                        System.out.println("");
                        System.out.println("请求错误，8秒后重试..");
                        System.out.println("");
                    }
                    if (stop) {
                        System.out.println(code);
                        System.out.println("调度完成,已成功获取数据，退出！");
                    } else {
                        i++;
                        Thread.sleep(8000);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

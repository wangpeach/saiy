package com.wly.scheduler;

import com.wly.service.CqsscService;
import com.wly.utils.Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Calendar;

@Service(value = "pullCodeScheduler")
public class PullCodeScheduler {

    @Resource
    private CqsscService cqsscService;
    private Calendar lastopen = null;

    public synchronized void exec() {
        try {
            //在某一时间段暂停开奖
            Calendar cal = Calendar.getInstance();

            Calendar stopday = Calendar.getInstance();

            stopday.setTime(cqsscService.dateFormat.parse(Utils.properties().getProperty("stopday")));

            Calendar startday = Calendar.getInstance();
            startday.setTime(cqsscService.dateFormat.parse(Utils.properties().getProperty("startday")));

            if(cal.before(stopday) || cal.after(startday)) {
                if(lastopen != null) {
                    System.out.println((Calendar.getInstance().getTime().getTime() - lastopen.getTime().getTime()) / (1000));
                    if(((Calendar.getInstance().getTime().getTime() - lastopen.getTime().getTime()) / (1000)) < 120) {
                        System.out.println("120秒之内重复调度,拦截启动...");
                    } else {
                        this.startPull();
                    }
                } else {
                    this.startPull();
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void startPull() {
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
                System.out.println("请求错误，3秒后重试..");
                System.out.println("");
            }
            if (stop) {
                lastopen = Calendar.getInstance();
                System.out.println(code);
                System.out.println("调度完成,已成功获取数据，退出！");
            } else {
                i++;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

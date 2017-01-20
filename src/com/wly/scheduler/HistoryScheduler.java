package com.wly.scheduler;

import com.wly.service.CqsscService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;

@Component
@Service(value = "historyScheduler")
public class HistoryScheduler {

    @Resource
    private CqsscService cqsscService;

    private Calendar cal = Calendar.getInstance();

    public void exec() {
        System.out.println("开始调度..");
        boolean stop = false;
        int i = 1;
        try {
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

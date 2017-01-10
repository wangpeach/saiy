package com.wly.service;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class InitializationProcessorService implements ApplicationListener<ContextRefreshedEvent>{

    @Resource
    private CqsscService service;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            //启动时同步今天最新数据
            service.holdCodes(null);
        }
    }
}

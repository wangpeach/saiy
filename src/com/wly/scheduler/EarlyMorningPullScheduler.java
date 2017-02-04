package com.wly.scheduler;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Component
@Service(value = "earlyMorningPullScheduler")
public class EarlyMorningPullScheduler {

    @Resource
    private PullCodeScheduler pullCodeScheduler;

    public void exec() {
        pullCodeScheduler.exec();
    }
}

package com.wly.service;

import javax.annotation.Resource;

/**
 * Created by rjora on 2017/2/16 0016.
 */
public class LastSyned extends Thread {

    @Resource
    private CqsscService service;

    public void run() {
        try {
            Thread.sleep(6000);
            service.lastNoSyn = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

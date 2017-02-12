package com.wly.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by Ant on 2017/2/7.
 */
public class OnlineSessionLintener implements HttpSessionListener {

    OnlineCencus online = OnlineCencus.getInstance();

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        online.add();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        online.cut();
    }
}

package com.wly.listener;


import com.google.gson.Gson;
import com.wly.utils.FileOperate;
import com.wly.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ant on 2017/2/7.
 */
public class OnlineCencus {

    Gson gson = new Gson();

    public OnlineCencus () {
        this.maxOnlineMth(null);
    }

    private void maxOnlineMth(String maxOnline) {
        int max = 0;
        String configstr = null;
        Map<String, Object> config = null;
        String configsrc = Utils.properties().getProperty("config");
        if(FileOperate.isExists(configsrc)) {
            configstr = FileOperate.readfile(configsrc);
            config = gson.fromJson(configstr, Map.class);
            if(Utils.isNotNullOrEmpty(maxOnline)) {
                max = Integer.parseInt(maxOnline);
                config.put("maxOnline", maxOnline);
                this.holdConfig(config, configsrc);
            } else {
                max = Integer.parseInt(config.get("maxOnline").toString());
            }
            this.maxOnline = max;
        } else {
            config = new HashMap<String, Object>();
            config.put("maxOnline", "0");
            this.holdConfig(config, configsrc);
        }
    }

    private void holdConfig(Map<String, Object> arg, String path) {
        String configstr = gson.toJson(arg);
        FileOperate.saveFile(configstr, path, true);
    }

    private static final OnlineCencus onlineSender = new OnlineCencus();

    private int maxOnline = 0;
    private int curOnline = 0;

    public static OnlineCencus getInstance() {
        return onlineSender;
    }

    public void add() {
        curOnline++;
        if(curOnline > maxOnline) {
            this.maxOnlineMth(String.valueOf(curOnline));
        }
    }

    public void cut() {
        if(curOnline > 0) {
            curOnline--;
        }
    }

    public int getCurOnline() {
        return curOnline;
    }

    public int getMaxOnline() {
        return maxOnline;
    }


}

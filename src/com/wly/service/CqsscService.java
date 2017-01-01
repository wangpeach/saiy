package com.wly.service;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.wly.utils.FileOperate;
import com.wly.utils.Utils;
import com.wly.utils._HttpConnection;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CqsscService extends BaseService {

    //	private static final String url = "http://f.apiplus.cn/cqssc-%s.json";
    private static final String url = "http://t.apiplus.cn/%s.do?token=demo&code=cqssc&format=json";
    public static final String SAVEPAHT = "D://lshm//";


    /**
     * 获取某一天的数据
     *
     * @param day
     * @return
     */
    public String synchronize(String day) {
        String codes = null;
        Calendar cal = Calendar.getInstance();
        String curday = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
        // 获取历史数据
        if (Utils.isNotNullOrEmpty(day) && !curday.equals(day)) {
            codes = this.readCodes(day);
        }
        //获取今天数据
        if (!Utils.isNotNullOrEmpty(codes)) {
            //今天数据是否已经缓存，已缓存直接返回
            ActionContext context = ServletActionContext.getContext();
            Map<String, Object> app = context.getApplication();

            if(app.get("codes") != null) {
                codes = app.get("codes").toString();
            }
            if(!Utils.isNotNullOrEmpty(codes)) {
                codes = this.reqHaoMa(-1, curday);
                app.put("codes", codes);
                context.setApplication(app);
            }
        }
        return codes;
    }

    /**
     * 请求指定条件数据
     *
     * @param limit 偏移量
     * @param day   指定日期数据
     * @return
     */
    public String reqHaoMa(int limit, String day) {

        Map<String, Object> arg = new HashMap<String, Object>();
        String reqUrl = "";
        if (Utils.isNotNullOrEmpty(limit) && limit > 0) {
            //请求指定条数数据
            reqUrl = String.format((url + ("&rows=" + limit)), "newly");
        }
        if (Utils.isNotNullOrEmpty(day)) {
            //请求全天数据
            reqUrl = String.format((url + ("&date=" + day)), "daily");
        }
        _HttpConnection conn = new _HttpConnection(_HttpConnection.HttpType.http, _HttpConnection.HttpMethod.GET);
        String result = conn.sendRequest(reqUrl, arg);
        return this.toJson(this.handleJson(result));
    }

    /**
     * 读取本地文件数据
     *
     * @param day
     * @return
     */
    public String readCodes(String day) {
        String codes = null;
        String path = SAVEPAHT + day + ".json";
        if (FileOperate.isExists(path)) {
            codes = FileOperate.readfile(path);
        } else {
            //本地文件不存在情况下请求数据后保存
            codes = holdCodes(day);
        }
        return codes;
    }

    /**
     * 保存当天数据到文件
     * D://lshm//yyyy-MM-dd.json
     *
     * @param date
     * @return 数据
     */
    public String holdCodes(String date) {
        String day = date;
        if (!Utils.isNotNullOrEmpty(day)) {
            Calendar cal = Calendar.getInstance();
            day = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
        }
        String path = SAVEPAHT + day + ".json";
        //获取项目目录
        //FileOperate.getRootPath("saiy");
        //获取历史
        String codesJson = this.reqHaoMa(-1, day);
        FileOperate.createNewFile(codesJson, path);
        return codesJson;
    }

    /**
     * 存放最近数据
     * @param json
     */
    public void putCode(String json) {
        ActionContext context = ServletActionContext.getContext();
        Map<String, Object> app = context.getApplication();

        String codesJson = app.get("codes").toString();

        Gson gson = new Gson();

        Map<String, Object> code = (Map<String, Object>)gson.fromJson(json, List.class).get(0);

        List<Map<String, Object>> codes = gson.fromJson(codesJson, List.class);
        codes.add(0, code);

        app.put("codes", this.toJson(codes));
        context.setApplication(app);
    }

    public List<Map<String, Object>> handleJson(String json) {
        Gson gson = new Gson();
        Map<String, Object> map  =gson.fromJson(json, Map.class);
        List<Map<String, Object>> codes = (List<Map<String, Object>>) map.get("data");
        return codes;
    }

    public String toJson(List<Map<String, Object>> codes) {
        Gson gson = new Gson();
        return gson.toJson(codes, List.class);
    }
}

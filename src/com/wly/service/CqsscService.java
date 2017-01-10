package com.wly.service;

import com.google.gson.Gson;
import com.wly.utils.FileOperate;
import com.wly.utils.Utils;
import com.wly.utils._HttpConnection;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CqsscService extends BaseService {

    private static final String url = "http://t.apiplus.cn/%s.do?token=demo&code=cqssc&format=json";
    public static final String SAVEPAHT = "D://lshm//";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 获取某一天的数据
     *
     * @param day
     * @return
     */
    public String synchronize(String day) {

        String codes = null;
        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.MONTH, 1);
        String curday = dateFormat.format(cal.getTime());
        // 获取历史数据
        if (!Utils.isNotNullOrEmpty(day)) {
            day = curday;
        }
        codes = this.readCodes(day);
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
//        System.out.println("测试：" + ServletActionContext.getServletContext().getAttribute("curterm"));
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
        result = this.toJson(this.handleJson(result));
        return result;
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

    public String getLastTerm(String term) {

        return null;
    }

    /**
     * 保存当天数据到文件
     * D://lshm//yyyy-MM-dd.json
     *
     * @param day
     * @return 数据
     */
    public String holdCodes(String day) {
        if (!Utils.isNotNullOrEmpty(day)) {
            Calendar cal = Calendar.getInstance();
            day = dateFormat.format(cal.getTime());
        }
        String path = SAVEPAHT + day + ".json";
        //获取项目目录
        //FileOperate.getRootPath("saiy");
        //获取历史
        String codesJson = this.reqHaoMa(-1, day);
        FileOperate.saveFile(codesJson, path, true);
        return codesJson;
    }


    /**
     * 存放最近数据
     *
     * @param json 需要存放的数据
     * @return 是否已存放
     */
    public boolean putCode(String json) {
        boolean puted = false;
        Gson gson = new Gson();
        Map<String, Object> code = (Map<String, Object>) gson.fromJson(json, List.class).get(0);

        Calendar cal = Calendar.getInstance();
        //最后一期数据在24:01左右开奖,所以日期向前推1天
        if (code.get("expect").toString().endsWith("120")) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        String day  = dateFormat.format(cal.getTime());

        String codesJson = "";
        String path = SAVEPAHT + day + ".json";
        if (FileOperate.isExists(path)) {
            codesJson = FileOperate.readfile(path);
        }

        List<Map<String, Object>> codes = null;

        if (!Utils.isNotNullOrEmpty(codesJson)) {
            codes = new ArrayList<Map<String, Object>>();
            codes.add(0, code);
            FileOperate.saveFile(this.toJson((codes)), path, true);
            puted = true;
        } else {
            codes = gson.fromJson(codesJson, List.class);
            //新的一期
            if (!codes.get(0).get("expect").equals(code.get("expect"))) {
                codes.add(0, code);
                FileOperate.saveFile(this.toJson((codes)), path, true);
                puted = true;
            }
        }
        return puted;
    }

    /**
     * 处理请求的数据，只需要开奖号码
     *
     * @param json
     * @return
     */
    public List<Map<String, Object>> handleJson(String json) {
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(json, Map.class);
        List<Map<String, Object>> codes = (List<Map<String, Object>>) map.get("data");
        return codes;
    }

    /**
     * 将开奖号码转换成json数据
     *
     * @param codes
     * @return
     */
    public String toJson(List<Map<String, Object>> codes) {
        Gson gson = new Gson();
        return gson.toJson(codes, List.class);
    }

}

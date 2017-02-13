package com.wly.service;

import com.google.gson.Gson;
import com.wly.utils.FileOperate;
import com.wly.utils.Utils;
import com.wly.utils._HttpConnection;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wly.utils.Utils.properties;

@Service
public class CqsscService extends BaseService {

    private static final String url = "http://a.apiplus.net/%s.do?token=937f08385b8734c6&code=cqssc&format=json";
    private String savePath = "";
    public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String curDay = null;
    private Gson gson = new Gson();

    public CqsscService() {
        this.savePath = properties().getProperty("holdPath");
    }

    /**
     * 日志
     * @param arg
     */
    private void log(String arg) {
        String logstr = "";
        List<String> logs = null;
        if(Utils.properties().getProperty("logstore").equals("1")) {
            String logsrc = Utils.properties().getProperty("log");
            if(FileOperate.isExists(logsrc)) {
                logstr = FileOperate.readfile(logsrc);
                logs  = gson.fromJson(logstr, List.class);
                if(logs != null && logs.size() > 0) {
                    logs.add(arg);
                    FileOperate.saveFile(gson.toJson(logs), logsrc, true);
                }
            } else {
                logs = new ArrayList<String>();
                logs.add(arg);
                FileOperate.saveFile(gson.toJson(logs), logsrc, true);
            }
        }
    }



    /**
     * 获取某一天的数据
     *
     * @param day
     * @return
     */
    public String synchronize(String day, String clientTime) {
        int seconds = 0;
        String codes = null;
        Map<String, Object> map = new HashMap<String, Object>();
        // 获取历史数据
        if (!Utils.isNotNullOrEmpty(day)) {
            Calendar cal = Calendar.getInstance();
            if(cal.get(Calendar.HOUR_OF_DAY) == 0 && cal.get(Calendar.MINUTE) == 0 && cal.get(Calendar.SECOND) <= 40) {
                cal.add(Calendar.DAY_OF_MONTH, -1);
            }
            day = dateFormat.format(cal.getTime());
        }
        codes = this.readCodes(day);
        List<Map<String, Object>> _codes = gson.fromJson(codes, List.class);

        if(codes.startsWith("-*-")) {
            codes = codes.replace("-*-", "");
            map.put("msg", codes);
        } else {
            map.put("codes", _codes);
        }

        if(Utils.isNotNullOrEmpty(clientTime)) {
            try {
                Date _clientTime = this.dateTimeFormat.parse(clientTime);
                long lngClient = _clientTime.getTime();
                long lngServer = Calendar.getInstance().getTime().getTime();
                seconds = (int) (lngServer - lngClient) / 1000;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            map.put("syntime", seconds);
        }
        return gson.toJson(map);
    }

    /**
     * 请求指定条件数据
     *
     * @param limit 偏移量
     * @param day   指定日期数据
     * @return
     */
    public synchronized String reqHaoMa(int limit, String day) {

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
        String result = null;
        try {
            String log = "request url: " + reqUrl + "; datetime:" + dateTimeFormat.format(Calendar.getInstance().getTime());
            System.out.println(log);
            this.log(log);
            result = this.toJson(this.handleJson(conn.sendRequest(reqUrl, arg)));
        } catch (IOException e) {
            result = null;
            System.out.println(e);
        }
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
        String path = savePath + day + ".json";
        if (FileOperate.isExists(path)) {
            codes = FileOperate.readfile(path);
            // 如果不是读取今天的数据，检查数据是否完整，不完整则重新请求数据
            curDay = dateFormat.format(Calendar.getInstance().getTime());
            if(!curDay.equals(day)) {
                int codeSize = gson.fromJson(codes, List.class).size();
                if(codeSize < 120) {
                     codes = this.holdCodes(day);
                }
            }
        } else {
            //本地文件不存在情况下请求数据后保存
            codes = holdCodes(day);
        }
        return codes;
    }

    /**
     * 从本地获取今天最新一期数据
     * @return
     */
    public Map<String, Object> getLastTerm() {
        Calendar cal = Calendar.getInstance();
        //获取第120期数据
        if(cal.get(Calendar.HOUR_OF_DAY) == 0 && cal.get(Calendar.MINUTE) < 4) {
            cal.add(Calendar.DAY_OF_MONTH, - 1);
        }
        curDay = dateFormat.format(cal.getTime());
        String codesStr = this.readCodes(curDay);
        List<Map<String, Object>> codes =  gson.fromJson(codesStr, List.class);
        return codes.get(0);
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
            day = dateFormat.format(Calendar.getInstance().getTime());
        }
        String codesJson = null;
        try {
            //在某一时间段暂停开奖
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFormat.parse(day));

            Calendar stopday = Calendar.getInstance();
            stopday.setTime(dateTimeFormat.parse(properties().getProperty("stopday")));

            Calendar startday = Calendar.getInstance();
            startday.setTime(dateTimeFormat.parse(properties().getProperty("startday")));

            if(cal.before(stopday) || cal.after(startday)) {
                String path = savePath + day + ".json";
                //获取历史
                while(codesJson == null) {
                    codesJson = this.reqHaoMa(-1, day);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                List<Map<String, Object>> codes = gson.fromJson(codesJson, List.class);
                if(codes != null && codes.size() > 0) {
                    Map<String, Object> firstCode = codes.get(codes.size() - 1);

                    int term = Integer.parseInt(firstCode.get("expect").toString().substring(8, 11));
                    if(term > 1) {
                        String strterm = firstCode.get("expect").toString().substring(0, 8);
                        for (int i = term; i > 1; i--) {
                            Map<String, Object> additional = new HashMap<String, Object>();
                            int tempterm = term - 1;
                            int termlen = 3 - String.valueOf(tempterm).length();
                            for (int j = 0; j < termlen; j++) {
                                strterm += "0";
                            }
                            additional.put("expect", strterm + tempterm);
                            additional.put("opencode", "");
                            codes.add(codes.size(), additional);
                        }
                        codesJson = gson.toJson(codes);
                    }
                    FileOperate.saveFile(codesJson, path, true);
                }
            } else {
                try {
                    String msg = new String(properties().getProperty("msg").getBytes("iso-8859-1"), "GBK");
                    codesJson = "-*-" + msg;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        Map<String, Object> code = (Map<String, Object>) gson.fromJson(json, List.class).get(0);

        Calendar cal = Calendar.getInstance();
        //最后一期数据在24:01左右开奖,所以日期向前推1天
        if (code.get("expect").toString().endsWith("120")) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        String day  = dateFormat.format(cal.getTime());

        String codesJson = "";
        String path = savePath + day + ".json";
        if (FileOperate.isExists(path)) {
            codesJson = FileOperate.readfile(path);
        }

        List<Map<String, Object>> codes = null;

        if (!Utils.isNotNullOrEmpty(codesJson)) {
            codes = new ArrayList<Map<String, Object>>();
            codes.add(code);
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
     * 统计近7天开奖号码形态
     * @return
     */
    public String cencusLast7() {
        Calendar cal = Calendar.getInstance();
        List<Map<String, Object>> last7 = new ArrayList<Map<String, Object>>();
        for (int i = 1; i < 8; i++) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
            String day = dateFormat.format(cal.getTime());
            String codes = this.readCodes(day);

            Map<String, Object> res = null;
            if(!codes.startsWith("-*-")) {
                res = this.analyCurb(codes);
            } else {
                res = new HashMap<String, Object>();
                res.put("zu6", "--");
                res.put("zu3", "--");
                res.put("baozi", "--");
                res.put("duizi", "--");
                res.put("shun", "--");
            }
            res.put("date", dateFormat.format(cal.getTime()));
            last7.add(res);
        }
        String result = gson.toJson(last7);
        return result;
    }

    /**
     * 分析组六，组三，豹子，前后二对子, 统计数量
     * @param jsonCodes 某一天的开奖号码
     * @return 各形态数量
     */
    private Map<String, Object> analyCurb(String jsonCodes) {
        Map<String, Object> result = new HashMap<String, Object>();
        int zu6 = 0, zu3 = 0, baozi = 0, duizi = 0, shun = 0;

        List<Map<String, Object>> codes = gson.fromJson(jsonCodes, List.class);

        List<Map<String, Object>> analyRes = new ArrayList<Map<String, Object>>();

        if(codes != null && codes.size() > 0) {

            for (int i = 0; i < codes.size(); i++) {
                String code = codes.get(i).get("opencode").toString().replace(",", "");
                if(Utils.isNotNullOrEmpty(code)) {
                    analyRes.add(_3Analy(code.substring(0, 3)));
                    analyRes.add(_3Analy(code.substring(1, 4)));
                    analyRes.add(_3Analy(code.substring(2, 5)));
                    analyRes.add(_2Analy(code.substring(0, 2)));
                    analyRes.add(_2Analy(code.substring(3, 5)));
                }
            }
        }

        for (Map<String, Object> item: analyRes) {
            if(item != null && item.size()> 0 && item.get("type") != null) {
                String type = item.get("type").toString();
                switch (type) {
                    case "zu6":
                        zu6++;
                        break;
                    case "zu3":
                        zu3++;
                        break;
                    case "baozi":
                        baozi++;
                        break;
                    case "duizi":
                        duizi++;
                        break;
                    case "shun":
                        shun++;
                        break;
                }
            }
        }
        result.put("zu6", zu6);
        result.put("zu3", zu3);
        result.put("baozi", baozi);
        result.put("duizi", duizi);
        result.put("shun", shun);
        return result;
    }

    /**
     * 分析前中后三
     * @param codeStr
     * @return
     */
    private Map<String, Object> _3Analy (String codeStr) {
        char[] code = codeStr.toCharArray();
        Map<String, Object> result = new HashMap<String, Object>();
        int maxcter = 0;
        for (int i = 0; i < code.length; i++) {
            int counter = 0;
            for (int j = 0; j < code.length; j++) {
                if(i != j) {
                    if(code[i] == code[j]) {
                        counter++;
                        if(counter > maxcter) {
                            maxcter = counter;
                        }
                    }
                }
            }
            if(maxcter == 1) {
                result.put("type", "zu3");
                result.put("text", "组三");
            } else if(maxcter == 2) {
                result.put("type", "baozi");
                result.put("text", "豹子");
            } else {
                result.put("type", "zu6");
                result.put("text", "组六");
            }
        }
        return result;
    }

    /**
     * 分析前后二
     * @param codeStr
     * @return
     */
    private Map<String, Object> _2Analy(String codeStr) {
        char[] code = codeStr.toCharArray();
        Map<String, Object> result = new HashMap<String, Object>();
        if(code[0] == code[1]) {
            result.put("type", "duizi");
            result.put("text", "对子");
        } else {
            Pattern pattern = Pattern.compile("^[0-9]*$");
            Matcher m = pattern.matcher(codeStr);
            if(m.matches()) {
                int _positive_1_9_1 = Math.abs(Integer.parseInt(String.valueOf(code[0])) - Integer.parseInt(String.valueOf((code[1]))));
                int _positive_1_9_2 = Math.abs(Integer.parseInt(String.valueOf(code[1])) - Integer.parseInt(String.valueOf(code[0])));
                if(_positive_1_9_1 == 1 || _positive_1_9_1 == 9 || _positive_1_9_2 == 1 || _positive_1_9_2 == 9) {
                    result.put("type", "shun");
                    result.put("text", "连号");
                }
            }
        }
        return result;
    }

    /**
     * 处理请求的数据，只需要开奖号码
     * @param json
     * @return
     */
    public List<Map<String, Object>> handleJson(String json) {
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
        return gson.toJson(codes, List.class);
    }

}

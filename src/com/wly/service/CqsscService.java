package com.wly.service;

import com.wly.utils.Utils;
import com.wly.utils._HttpConnection;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CqsscService extends BaseService {

//	private static final String url = "http://f.apiplus.cn/cqssc-%s.json";
	private static final String url = "http://t.apiplus.cn/%s.do?token=demo&code=cqssc&format=json";


    /**
     * 获取某一天的数据
     * @param day
     * @return
     */
	public String synchronize(String day) {
		return this.reqHaoMa(-1, day);
	}

	/**
	 * 请求指定条件数据
	 * @param limit 偏移量
	 * @return
	 */
	public String reqHaoMa(int limit, String day) {
		Map<String, Object> arg = new HashMap<String, Object>();
		String reqUrl = "";
		if(Utils.isNotNullOrEmpty(limit) && limit > 0) {
            //请求指定条数数据
            reqUrl = String.format((url + ("&rows="+limit)), "newly");
		}
		if(Utils.isNotNullOrEmpty(day)) {
            //请求全天数据
            reqUrl = String.format((url + ("&date="+day)), "daily");
		}
        _HttpConnection conn = new _HttpConnection(_HttpConnection.HttpType.http, _HttpConnection.HttpMethod.GET);
		String result = conn.sendRequest(reqUrl, arg);
		return result;
	}


}

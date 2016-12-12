package com.wly.service;

import com.google.gson.Gson;
import com.wly.utils.HttpConnection;
import com.wly.utils.HttpConnection.HttpType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CqsscService extends BaseService {

	private static final String url = "http://www.cp66607.com/api/cqssc";


	public String synchronize(String day) {
		String start = day + "001";
 		Gson gson = new Gson();
		Map<String, Object> map = gson.fromJson(this.reqHaoMa(1), Map.class);
		System.out.println(map);
		return null;
	}

	/**
	 * 请求历史号码
	 * @param limit 偏移量
	 * @return
	 */
	public String reqHaoMa(int limit) {
		Map<String, Object> arg = new HashMap<String, Object>();
		arg.put("act", "lishikaijianghaoma");
		arg.put("limit", limit);
		HttpConnection conn = new HttpConnection(HttpType.http);
		String result = conn.sendRequest(url, arg);
		return null;
	}


}

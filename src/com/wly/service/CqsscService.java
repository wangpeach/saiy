package com.wly.service;

import com.wly.utils.FileOperate;
import com.wly.utils.Utils;
import com.wly.utils._HttpConnection;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
public class CqsscService extends BaseService {

//	private static final String url = "http://f.apiplus.cn/cqssc-%s.json";
	private static final String url = "http://t.apiplus.cn/%s.do?token=demo&code=cqssc&format=json";
	public static final String SAVEPAHT = "D://lshm//";


    /**
     * 获取某一天的数据
     * @param day
     * @return
     */
	public String synchronize(String day) {
		String codes = null;
		Calendar cal = Calendar.getInstance();
		String curday = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
		if(!day.equals(curday)) {
			codes = this.readCodes(day);
		}
		if(!Utils.isNotNullOrEmpty(codes)) {
			codes = this.reqHaoMa(-1, day);
		}
		return codes;
	}

	/**
	 * 请求指定条件数据
	 * @param limit 偏移量
	 * @param day	 指定日期数据
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

	/**
	 * 读取本地文件数据
	 * @param day
	 * @return
	 */
	public String readCodes(String day) {
		String codes = null;
		String path = SAVEPAHT + day + ".json";
		if(FileOperate.isExists(path)) {
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
	 * @param date
	 * @return 数据
	 */
	public String holdCodes(String date) {
		String day = date;
		if(!Utils.isNotNullOrEmpty(day)) {
			Calendar cal = Calendar.getInstance();
			day = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
		}
		String path = SAVEPAHT + day + ".json";
		//获取项目目录
		//FileOperate.getRootPath("saiy");
		//获取历史
		String codes = this.reqHaoMa(-1, day);
		FileOperate.createNewFile(codes, path);
		return codes;
	}
}

package com.wly.scheduler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wly.utils._HttpConnection;
import com.wly.utils._HttpConnection.HttpMethod;
import com.wly.utils._HttpConnection.HttpType;
import com.wly.utils.Utils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service(value = "weChatService")
@Component
public class WeChatCore {
	
	private static String appID = "wx00d42811e11c7833";
	
	private static String appsecret = "72bf1aaae9af10b93fe91e2314e93e78";
	
	public static String getAppID() {
		return appID;
	}

	public static String getAppsecret() {
		return appsecret;
	}

	/**
	 * 更新 Token
	 */
	public void flush_access_token() {
		System.out.println("START UPDATE TOKEN..");
		Token token = Token.getInstance();
		token.setRenew(true);
		Map<String, Object> arg = new HashMap<String, Object>();
		arg.put("grant_type", "client_credential");
		arg.put("appid", appID);
		arg.put("secret", appsecret);
		_HttpConnection conn = new _HttpConnection(HttpType.https, HttpMethod.GET);
		String tokenJson = conn.sendRequest(WXInterface.TOKEN_URL, arg);
		if(Utils.isNotNullOrEmpty(tokenJson)) {
			JsonParser gson = new JsonParser();
			JsonObject jo = gson.parse(tokenJson).getAsJsonObject();
			
			token.setAccessToken(jo.get("access_token").getAsString());
			token.setExpiresIn(jo.get("expires_in").getAsString());
			token.setRenew(false);
			System.out.println("TNE END..");
		}
	}
	
	/**
	 * 获取 Token
	 * @return
	 */
	public static String getToken() {
		return Token.getInstance().getAccessToken();
	}
	
	/**
	 * 获取微信IP地址
	 * @return
	 */
	public static String getWxIps() {
		Map<String, Object> arg = new HashMap<String, Object>();
		arg.put("access_token", Token.getInstance().getAccessToken());
		_HttpConnection conn = new _HttpConnection(HttpType.https, HttpMethod.GET);
		return conn.sendRequest(WXInterface.WXIP, arg);
	}
	
	public static String createMenu(String menuJson) {
		String createMenuUrl = String.format("%s?access_token=%s", WXInterface.CREATE_MENU, Token.getInstance().getAccessToken());
		_HttpConnection conn = new _HttpConnection(HttpType.https, HttpMethod.POST);
		return conn.sendRequest(createMenuUrl, menuJson);
	}
	
	public static String getMenu() {
		Map<String, Object> arg = new HashMap<String, Object>();
		arg.put("access_token", Token.getInstance().getAccessToken());
		_HttpConnection conn = new _HttpConnection(HttpType.https, HttpMethod.POST);
		return conn.sendRequest(WXInterface.GET_MENU, arg);
	}
}

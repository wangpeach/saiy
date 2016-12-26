package com.wly.utils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class _HttpConnection {

	public _HttpConnection(HttpType arg) {
		this.httpType = arg;
	}

	public _HttpConnection(HttpType arg, int timeout) {
		this.timeout = timeout;
		this.httpType = arg;
	}

	public _HttpConnection(HttpType arg, HttpMethod httpMethod) {
		this.requestMethod = httpMethod;
		this.httpType = arg;
	}

	public _HttpConnection(HttpType arg, HttpMethod httpMethod, int timeout) {
		this.httpType = arg;
		this.requestMethod = httpMethod;
		this.timeout = timeout;
	}
	
	private int timeout = 3000;
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	private HttpType httpType = HttpType.http;
	
	public HttpType getHttpType() {
		return httpType;
	}

	public void setHttpType(HttpType httpType) {
		this.httpType = httpType;
	}

	private HttpMethod requestMethod = HttpMethod.GET;
	private String httpMethod = "GET";
	
	public HttpMethod getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(HttpMethod requestMethod) {
		this.requestMethod = requestMethod;
		if(requestMethod.equals(HttpMethod.GET)) {
			this.httpMethod = "GET";
		} else if(requestMethod.equals(HttpMethod.POST)) {
			this.httpMethod = "POST";
		} else if(requestMethod.equals(HttpMethod.HEAD)) {
			this.httpMethod = "HEAD";
		} else if(requestMethod.equals(HttpMethod.OPTIONS)) {
			this.httpMethod = "OPTIONS";
		} else if(requestMethod.equals(HttpMethod.PUT)) {
			this.httpMethod = "PUT";
		} else if(requestMethod.equals(HttpMethod.DELETE)) {
			this.httpMethod = "DELETE";	
		} else {
			this.httpMethod = "TRACE";
		}
	}
	
	public String sendRequest(String arg, String arg1) {
		String complete = "";
		URL url;
		try {
			url = new URL(arg);
			HttpsURLConnection connect = (HttpsURLConnection)url.openConnection();
			connect.setRequestMethod(this.httpMethod);
			connect.setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					// TODO Auto-generated method stub
					return true;
				}
			});

			connect.setRequestProperty("Content-Type",
			        "application/x-www-form-urlencoded");
			//设置输入输出参数
			connect.setDoInput(true);
			connect.setDoOutput(true);

			connect.setConnectTimeout(timeout);

			DataOutputStream out = new DataOutputStream(connect.getOutputStream());
			//输入参数
			out.write(arg1.getBytes("utf-8"));

			//发起请求
			InputStream is = connect.getInputStream();

		    BufferedReader rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
		    StringBuilder result = new StringBuilder();
		    String line;
		    while((line = rd.readLine()) != null) {
		    	result.append(line);
		    }
		    rd.close();
			out.flush();
			out.close();
			complete = result.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return complete;
	}

	/**
	 * 发送HTTP/HTTPS 请求
	 * @param arg1 请求地址
	 * @param arg2 请求参数
	 * @return
	 */
	public String sendRequest(String arg1, Map<String, Object> arg2) {
		String complete = null;
		URL url = null;
		try {
			url = new URL(arg1);
			//建立连接
			URLConnection _urlcon = null;
			if(httpType == HttpType.http) {
				HttpURLConnection connect = (HttpURLConnection)url.openConnection();
				connect.setRequestMethod(this.httpMethod);
				_urlcon = connect;
			} else {
				HttpsURLConnection connect = (HttpsURLConnection)url.openConnection();
				connect.setRequestMethod(this.httpMethod);
				connect.setHostnameVerifier(new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						// TODO Auto-generated method stub
						return true;
					}
				});
				_urlcon = connect;
			}
			_urlcon = this.setConnect(_urlcon);
			complete = this.send(_urlcon, arg2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(arg1);
			System.out.println("上面请求路径出错");
		}
		return complete;
	}
	
	/**
	 * 封装参数
	 * @param connect
	 * @return
	 */
	private URLConnection setConnect(URLConnection connect) {
		connect.setRequestProperty("Content-Type", 
		        "application/x-www-form-urlencoded");
		//设置输入输出参数
		connect.setDoInput(true);
		connect.setDoOutput(true);
		connect.setConnectTimeout(timeout);
		return connect;
	}
	
	/**
	 * 发送请求，返回响应 
	 * @param connect
	 * @param arg
	 * @return
	 */
	private String send(URLConnection connect, Map<String, Object> arg) {
		String complete = null;
		DataOutputStream out;
		try {
			out = new DataOutputStream(connect.getOutputStream());
			//输入参数
			String pv = "";
			StringBuilder sbr = new StringBuilder();
			if(arg != null && !arg.isEmpty()) {
				Set<String> paramNames = arg.keySet();
				Iterator<String> iterator = paramNames.iterator();
				while(iterator.hasNext()) {
					String paramName = iterator.next();
					sbr.append(paramName + "=");
					sbr.append(arg.get(paramName));
					sbr.append("&");
				}
				pv = sbr.substring(0, sbr.length()-1).toString();
			}
			System.out.println(pv);
			out.writeBytes(pv);
			
			//发起请求
			InputStream is = connect.getInputStream();
			
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
		    StringBuilder result = new StringBuilder();
		    String line;
		    while((line = rd.readLine()) != null) {
		    	result.append(line);
		    }
		    rd.close();
			out.flush();
			out.close();
			complete = result.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return complete;
	}
	
	public enum HttpType {
		http, https;
	}
	
	public enum HttpMethod {
		GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE;
	}
}

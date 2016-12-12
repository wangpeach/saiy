package com.wly.scheduler;

public class Token {
	
	private Token() {}
	private static class SingletonHolder {
		private static Token INSTANCE = new Token();
	}
	
	private String accessToken;
	
	private String expiresIn;
	//是否正在更新token
	private boolean renew;
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public String getExpiresIn() {
		return expiresIn;
	}
	
	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}
	
	public boolean isRenew() {
		return renew;
	}

	public void setRenew(boolean renew) {
		this.renew = renew;
	}

	public static Token getInstance() {
		Token token = SingletonHolder.INSTANCE;
		if(token.renew) {
			Token.getInstance();
		}
		return token;
	}
}

package com.wly.scheduler;

public class WXInterface {
    //获取 token 接口
    static String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    //微信IP 接口
    static String WXIP = "https://api.weixin.qq.com/cgi-bin/getcallbackip";
    //创建菜单接口
    static String CREATE_MENU = "https://api.weixin.qq.com/cgi-bin/menu/create";
    //获取菜单接口
    static String GET_MENU = "https://api.weixin.qq.com/cgi-bin/menu/get";
}

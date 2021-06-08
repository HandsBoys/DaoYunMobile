package com.fzu.daoyunmobile.Configs;

public class GlobalConfig {
    private static String USER_TOKEN = "";

    //设置token
    public static void setUserToken(String token) {
        USER_TOKEN = token;
    }

    //获取用户token
    public static String getUserToken() {
        return USER_TOKEN;
    }
}

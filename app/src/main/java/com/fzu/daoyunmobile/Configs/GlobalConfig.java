package com.fzu.daoyunmobile.Configs;

public class GlobalConfig {
    //用户Token
    private static String USER_TOKEN = "";

    //设置token
    public static void setUserToken(String token) {
        USER_TOKEN = token;
    }

    //获取用户token
    public static String getUserToken() {
        return USER_TOKEN;
    }

    //用户ID
    private static String USER_ID = "";

    //设置用户ID
    public static void setUserID(String id) {
        USER_ID = id;
    }

    //获取用户ID
    public static String getUserID() {
        return USER_ID;
    }

    //用户手机号
    private static String USER_Phone = "";

    //设置手机号
    public static void setUserPhone(String phone) {
        USER_Phone = phone;
    }

    //获取用户手机号
    public static String getUserPhone() {
        return USER_Phone;
    }

    //用户名
    private static String USER_NAME = "";

    public static String getUserName() {
        return USER_NAME;
    }

    public static void setUserName(String userName) {
        USER_NAME = userName;
    }

    //用户昵称
    private static String NICK_NAME = "";

    public static String getNickName() {
        return NICK_NAME;
    }

    public static void setNickName(String nickName) {
        NICK_NAME = nickName;
    }

    //性别
    private static String SEX;

    public static String getSEX() {
        return SEX;
    }

    public static void setSEX(String SEX) {
        GlobalConfig.SEX = SEX;
    }


}

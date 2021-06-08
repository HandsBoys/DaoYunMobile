package com.fzu.daoyunmobile.Configs;


public class UrlConfig {
    private static String ORIGION_URL = "http://1.15.31.156:8081/";

    /**
     * 存放所有Url的配置文件
     */

    public enum UrlType {
        CODE_LOGIN,//验证码登录
        PSD_LOGIN,//账号密码登录
        QUiCK_REGISTER,//快速注册
        MESSAGE,//发送短信
        PIC_VERCODE,//获取图片验证码
        USER_INFO //获取用户信息
    }

    public static String getUrl(UrlType urlType) {
        switch (urlType) {
            case CODE_LOGIN:
                return ORIGION_URL + "login/sms";
            case PSD_LOGIN:
                return ORIGION_URL + "login";
            case QUiCK_REGISTER:
                return ORIGION_URL + "sign";
            case PIC_VERCODE:
                return ORIGION_URL + "captcha.jpg";
            case USER_INFO:
                return ORIGION_URL + "getInfo";
            case MESSAGE:
            default:
                return ORIGION_URL + "message?phone=";
        }
    }


}

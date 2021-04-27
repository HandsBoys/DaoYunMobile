package com.fzu.daoyunmobile.Configs;


public class UrlConfig {
    private static String ORIGION_URL = "http://1.15.31.156:8081/";

    /**
     * 存放所有Url的配置文件
     */

    public enum UrlType {
        CODE_LOGIN,
        PSD_LOGIN,
        QUiCK_REGISTER,
        MESSAGE
    }

    public static String getUrl(UrlType urlType) {
        switch (urlType) {
            case CODE_LOGIN:
                return ORIGION_URL + "login2";
            case PSD_LOGIN:
                return ORIGION_URL + "login3";
            case QUiCK_REGISTER:
                return ORIGION_URL + "sign2";
            case MESSAGE:
            default:
                return ORIGION_URL + "message?phone=";
        }
    }
}

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
        USER_INFO, //获取用户信息
        GET_JOINED_COURSES, //获取加入的班课
        GET_CREATED_COURSES, //获取创建的班课
        JOIN_COURSE,//加入班课
        CREATE_COURSE, //创建班课
        GET_DEPT,//获取院校信息
        GET_COURSE_INFO,//获取课程信息
        SET_JOIN_COURSE,//设置班级是否可加入
        SET_FINISH_COURSE,//设置班级是否结束
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
            case GET_JOINED_COURSES:
                return ORIGION_URL + "client/course/joined-course";
            case GET_CREATED_COURSES:
                return ORIGION_URL + "client/course/created-course";
            case CREATE_COURSE:
                return ORIGION_URL + "client/course/new-course";
            case JOIN_COURSE:
                return ORIGION_URL + "client/course/join-course";
            case GET_DEPT:
                return ORIGION_URL + "client/dept";
            case GET_COURSE_INFO:
                return ORIGION_URL + "client/course/query-course?id=";
            case SET_JOIN_COURSE:
                return ORIGION_URL + "client/course/set-join?";
            case SET_FINISH_COURSE:
                return ORIGION_URL + "client/course/set-finish?";
            case MESSAGE:
            default:
                return ORIGION_URL + "message?phone=";
        }
    }
}

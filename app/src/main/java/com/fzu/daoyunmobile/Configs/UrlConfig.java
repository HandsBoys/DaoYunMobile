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
        START_SIGN_IN,//发起签到
        GET_ALL_SIGN_IN,//获取所有签到
        STOP_SIGN_IN,//结束签到任务
        GET_DO_SIGN_IN,//获取进行中的签到任务
        DELETE_SIGN_IN,//删除签到任务
        GET_COURSE_ALL_STUDENT,//获取课程信息中的所有学生
        STUDENT_FINISH_SIGN_IN,//学生完成签到
        GET_SIGNIN_ALL_STUDENT_INFO,//获取某个签到中的所有学生信息
        GET_STUDENT_ALL_SIGN,//获取学生的所有签到信息
        STUDENT_ADD_SCORE,//给学生加分
        STUDENT_QUIT_CLASS,//http://1.15.31.156:8081/system/course/student/57/91
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
            case START_SIGN_IN:
                return ORIGION_URL + "client/checkin/start";
            case GET_ALL_SIGN_IN:
                return ORIGION_URL + "client/checkin/all-task?courseId=";//TODO注意添加签到信息
            case STOP_SIGN_IN:
                return ORIGION_URL + "client/checkin/end-task?checkinId=";
            case GET_DO_SIGN_IN:
                return ORIGION_URL + "client/checkin/active-task?courseId=";
            case DELETE_SIGN_IN:
                return ORIGION_URL + "client/checkin?checkinId=";
            case GET_COURSE_ALL_STUDENT:
                return ORIGION_URL + "client/management?courseId=";
            case STUDENT_FINISH_SIGN_IN:
                return ORIGION_URL + "client/checkin/finish-task";
            case GET_SIGNIN_ALL_STUDENT_INFO:
                return ORIGION_URL + "client/checkin/info?checkinId=";
            case GET_STUDENT_ALL_SIGN:
                return ORIGION_URL + "client/checkin/student-records?courseId=";
            case STUDENT_ADD_SCORE:
                return ORIGION_URL + "client/management/addscore?courseId=";
            case MESSAGE:
            case STUDENT_QUIT_CLASS:
                return ORIGION_URL + "client/course/quit-course?courseId=";
            default:
                return ORIGION_URL + "message?phone=";
        }
    }
}

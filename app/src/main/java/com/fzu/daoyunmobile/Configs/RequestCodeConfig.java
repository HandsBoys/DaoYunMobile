package com.fzu.daoyunmobile.Configs;

/**
 * 存放请求码
 */
public class RequestCodeConfig {
    //获取创建的课程
    private static final int CREATE_COURSE = 11001;

    public static int getCreateCourse() {
        return CREATE_COURSE;
    }

    //获取选择院校
    private static final int SELECT_FACULTY = 11002;

    public static int getSelectFaculty() {
        return SELECT_FACULTY;
    }

    //扫描请求
    private static final int SCANQR_CODE = 11003;

    public static int getScanqrCode() {
        return SCANQR_CODE;
    }

    //权限请求
    private static final int PERMISSION_REQUEST = 11004;

    public static int getPermissionRequest() {
        return PERMISSION_REQUEST;
    }

    //获取GPS请求代码
    private static int GPS_REQUEST_CODE = 11005;

    public static int getGpsRequestCode() {
        return GPS_REQUEST_CODE;
    }
}

package com.fzu.daoyunmobile.Configs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 存放一些用户信息等全局变量
 */
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

    //学校数据测试样例
    private static List<String> shcoolList = new ArrayList<String>() {//这个大括号 就相当于我们  new 接口
        {//这个大括号 就是 构造代码块 会在构造函数前 调用
            this.add("福州大学");//this 可以省略  这里加上 只是为了让读者 更容易理解
            add("福建师范大学");
            add("福建理工大学");
            add("福州大学2");//this 可以省略  这里加上 只是为了让读者 更容易理解
            add("福建师范大学2");
            add("福建理工大学2");
        }
    };

    public static void setShcoolList(List<String> sList) {
        shcoolList = sList;
    }

    //获取学校测试样例
    public static List<String> getShcoolList() {
        return shcoolList;
    }

    private static ArrayList<List<String>> facultyList = new ArrayList<List<String>>() {
        {
            this.add(Arrays.asList("化工", "数计", "人文"));
            add(Arrays.asList("化工1", "数计", "人文"));
            add(Arrays.asList("化工2", "数计", "人文"));
            add(Arrays.asList("化工3", "数计", "人文"));
            add(Arrays.asList("化工4", "数计231222", "人文", "FUCK"));
            add(Arrays.asList("化工5", "数计22123", "人文"));
            add(Arrays.asList("化工6", "数计22132", "人文"));
        }
    };


    public static void setFacultyList(ArrayList<List<String>> fList) {
        facultyList = fList;
    }

    public static ArrayList<List<String>> getFacultyList() {
        return facultyList;
    }

    //课程信息List
    private static List<String> courseList = new ArrayList<>();

    public static List<String> getCourseList() {
        return courseList;
    }

    public static void setCourseList(List<String> courseList) {
        GlobalConfig.courseList = courseList;
    }
}

package com.fzu.daoyunmobile.Entity;

public class Course {
    //班课号
    private String classId;
    //课程名
    private String courseName;
    //老师名
    private String teacherName;
    //班级名称 1班2班
    private String className;
    //班级上课时间
    private String courseDate = "";
    public String teacherPhone = "";


    public Course(String courseName, String teacherName, String className, String classId, String courseDate) {
        this.classId = classId;
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.className = className;
        this.courseDate = courseDate;
    }

    public Course(String courseName, String teacherName, String className, String classId) {
        this.classId = classId;
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.className = className;
    }

    public String getTeacherPhone() {
        return teacherPhone;
    }

    public String getClassId() {
        return classId;
    }

    public String getCourseDate() {
        return courseDate;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getClassName() {
        return className;
    }
}

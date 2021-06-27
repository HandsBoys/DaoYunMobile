package com.fzu.daoyunmobile.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fzu.daoyunmobile.Activities.ClassTabActivity;
import com.fzu.daoyunmobile.Adapter.MyCreateCourseAdapter;
import com.fzu.daoyunmobile.Configs.GlobalConfig;
import com.fzu.daoyunmobile.Configs.UrlConfig;
import com.fzu.daoyunmobile.Entity.Course;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;
import com.fzu.daoyunmobile.Utils.HttpUtils.OkHttpUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 我创建的课程视图
 */
public class MyCreateCourseFragment extends Fragment {

    public static List<Course> courseList = new ArrayList<>();
    private int myJoinNum = 0;
    public MyCreateCourseAdapter adapter;
    public ListView listView;
    public ProgressDialog progressDialog;

    public MyCreateCourseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_create_course, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("加载中...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        initCourses();
    }

    public void initCourses() {
        //获取用户信息
        OkHttpUtil.getInstance().GetWithToken(UrlConfig.getUrl(UrlConfig.UrlType.GET_CREATED_COURSES), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("GET CreateCourse" + e.getMessage());
                AlertDialogUtil.showToastText(e.getMessage(), getActivity());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {

                try {
                    String responseBodyStr = response.body().string();
                    if (GlobalConfig.getIsTeacher()) {
                        courseList = parseJsonWithJsonObject(responseBodyStr);

                    }
                    //  if (responseBodyStr.contains("Forbidden")) {}
                    afterAction();
                } catch (Exception e) {
                    //获取不到用户信息则取消登陆 需要重新登陆
                    AlertDialogUtil.showToastText(e.getMessage(), getActivity());
                    AlertDialogUtil.showConfirmClickAlertDialog(e.getMessage(), getActivity());
                }
            }
        });
    }

    private void afterAction() {
        getActivity().runOnUiThread(() -> {
            adapter = new MyCreateCourseAdapter(getContext(), R.layout.item_mycreatecourse, courseList, 2);
            listView = getActivity().findViewById(R.id.mycreatecourselist_view);
            listView.setAdapter(adapter);
            // listview点击事件
            listView.setOnItemClickListener((parent, view, position, id) -> {
                Course course = courseList.get(position);
                Intent intent = new Intent(getContext(), ClassTabActivity.class);
                intent.putExtra("courseName", course.getCourseName());
                intent.putExtra("classId", course.getClassId());
                intent.putExtra("enterType", "Create");
                intent.putExtra("teacherPhone", course.teacherPhone);
                intent.putExtra("term", course.getCourseDate());
                intent.putExtra("className", course.getClassName());
                startActivity(intent);
            });
            progressDialog.dismiss();
        });
    }

    private List<Course> parseJsonWithJsonObject(String jsonData) throws IOException {
        JSONArray jsonArray = JSONObject.parseObject(jsonData).getJSONArray("data");
        List<Course> cList = new ArrayList<>();
        List<String> courseList = GlobalConfig.getCourseList();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            final String classId = jsonObject.getString("id");
            final String courseName = jsonObject.getString("courseName");
            final String teacherName = jsonObject.getString("teacherName");
            //TODO 也是需要的 班级信息
            final String className = jsonObject.getJSONObject("classDto").getString("className");
            final String term = jsonObject.getString("term");
            courseList.add(courseName);
            Course course = new Course(R.drawable.course_img_1, courseName, teacherName, className, classId, term);
            course.teacherPhone = "1066666655";
            cList.add(course);
        }
        //List 去重
        HashSet h = new HashSet(courseList);
        courseList.clear();
        courseList.addAll(h);
        GlobalConfig.setCourseList(courseList);
        return cList;
    }
    

}
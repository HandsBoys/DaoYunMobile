package com.fzu.daoyunmobile.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fzu.daoyunmobile.Activities.ClassTabActivity;
import com.fzu.daoyunmobile.Adapter.MyJoinCourseAdapter;
import com.fzu.daoyunmobile.Configs.UrlConfig;
import com.fzu.daoyunmobile.Entity.Course;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;
import com.fzu.daoyunmobile.Utils.HttpUtils.OkHttpUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 加入的课程视图
 */
public class MyJoinCourseFragment extends Fragment {

    public List<Course> courseList = new ArrayList<>();
    public MyJoinCourseAdapter adapter;
    public ListView listView;
    private int myJoinNum = 0;
    public ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_join_course, container, false);
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

    //初始化课程
    public void initCourses() {
        //获取用户信息
        OkHttpUtil.getInstance().GetWithToken(UrlConfig.getUrl(UrlConfig.UrlType.GET_JOINED_COURSES), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                AlertDialogUtil.showToastText(e.getMessage(), getActivity());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                try {
                    String responseBodyStr = response.body().string();
                    if (responseBodyStr.contains("Forbidden")) {

                    } else {
                        courseList = parseJsonWithJsonObject(responseBodyStr);
                    }
                    afterAction();
                } catch (Exception e) {
                    //获取不到用户信息则取消登陆 需要重新登陆
                    AlertDialogUtil.showToastText(e.getMessage(), getActivity());
                    AlertDialogUtil.showConfirmClickAlertDialog("网络超时请重新打开APP", getActivity());
                }
            }
        });
    }

    private void afterAction() {
        getActivity().runOnUiThread(() -> {
            adapter = new MyJoinCourseAdapter(getContext(), R.layout.item_myjoincourse, courseList);
            listView = getActivity().findViewById(R.id.myjoincourselist_view);
            listView.setAdapter(adapter);
            //Item Click 传入需要的参数
            listView.setOnItemClickListener((parent, view, position, id) -> {
                Course course = courseList.get(position);
//                Toast.makeText(getContext(), course.getCourseName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), ClassTabActivity.class);

                intent.putExtra("courseName", course.getCourseName());
                intent.putExtra("classId", course.getClassId());
                // intent.putExtra("enterType", "create");
                intent.putExtra("term", course.getCourseDate());
                intent.putExtra("className", course.getClassName());

                intent.putExtra("enterType", "Join");
                intent.putExtra("teacherPhone", course.getTeacherPhone());
                startActivity(intent);
            });
            progressDialog.dismiss();
        });
    }

    private List<Course> parseJsonWithJsonObject(String jsonData) {

        JSONArray jsonArray = JSONObject.parseObject(jsonData).getJSONArray("data");
        List<Course> cList = new ArrayList<>();
        if (jsonArray == null)
            return cList;
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            final String classId = jsonObject.getString("id");
            final String courseName = jsonObject.getString("courseName");
            final String teacherName = jsonObject.getString("teacherName");
            final String className = jsonObject.getJSONObject("classDto").getString("className");
            final String term = jsonObject.getString("term");
            Course course = new Course(R.drawable.course_img_1, courseName, teacherName, className, classId, term);
            course.teacherPhone = "1066666655";
            cList.add(course);
        }
        Log.i("MyJoinListCourse", cList.size() + "");
        return cList;
    }
}
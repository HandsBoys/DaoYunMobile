package com.fzu.daoyunmobile.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fzu.daoyunmobile.Activities.ClassTabActivity;
import com.fzu.daoyunmobile.Adapter.MyJoinCourseAdapter;
import com.fzu.daoyunmobile.Entity.Course;
import com.fzu.daoyunmobile.R;

import java.util.ArrayList;
import java.util.List;

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
//        progressDialog = new ProgressDialog(getContext());
//        progressDialog.setMessage("加载中...");
//        progressDialog.setCancelable(true);
//        progressDialog.show();
        initCourses();

    }

    //初始化课程
    private void initCourses() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OkHttpClient okHttpClient = new OkHttpClient();
//                RequestBody requestBody = new FormBody.Builder()
//                        .add("phoneNumber", MainActivity.phoneNumber)
//                        .build();
//                Request request = new Request.Builder()
//                        .url("http://47.98.236.0:8080/myjoinclass")
//                        .post(requestBody)
//                        .build();
//                okHttpClient.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
////                        Toast.makeText(getContext(), "Connection failed!", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                        String responseBodyStr = response.body().string();
//                        Log.i("LoginInfo", responseBodyStr);
        courseList = parseJsonWithJsonObject("responseBodyStr");
        afterAction();
//                    }
//                });
//            }
//        }).start();
    }

    private void afterAction() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new MyJoinCourseAdapter(getContext(), R.layout.myjoincourse_frameitem_layout, courseList);
                listView = getActivity().findViewById(R.id.myjoincourselist_view);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    //Item Click 传入需要的参数
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Course course = courseList.get(position);
//                Toast.makeText(getContext(), course.getCourseName(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), ClassTabActivity.class);
                        intent.putExtra("courseName", course.getCourseName());
                        intent.putExtra("classId", course.getClassId());
                        intent.putExtra("enterType", "join");
                        intent.putExtra("teacherPhone", course.getTeacherPhone());
                        startActivity(intent);
                    }
                });
//                progressDialog.dismiss();
            }
        });
  //      progressDialog.dismiss();
    }

    private List<Course> parseJsonWithJsonObject(String jsonData) {
//        try {
//            File classFile = new File(Environment.getExternalStorageDirectory() + "/daoyun/"
//                    + MainActivity.phoneNumber + "_join.json");
//            if (classFile.exists()) {
//                classFile.delete();
//            }
//            classFile.createNewFile();
//            byte[] bt = new byte[4096];
//            bt = jsonData.getBytes();
//            FileOutputStream out = new FileOutputStream(classFile);
//            out.write(bt, 0, bt.length);
//            out.close();
//
//            JSONArray jsonArray = new JSONArray(jsonData);
        List<Course> cList = new ArrayList<Course>();
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                final String classId = jsonObject.getString("classId");
//                final String className = jsonObject.getString("className");
//                final String teacherName = jsonObject.getString("teacherName");
//                final String gradeClass = jsonObject.getString("gradeClass");
//                final String classIcon = jsonObject.getString("classIcon");
//                final String teacherPhone = jsonObject.getString("teacherPhone");
        final Course[] course = {null};
//                File classIconFile = null;
//                if (classIcon.equals("")) {
//                    if (teacherName == null) {
//                        course[0] = new Course(R.drawable.course_img_1, className, "", gradeClass, classId);
//                    } else {
        course[0] = new Course(R.drawable.course_img_1, "工程训练", "陈哥", "1班", "566", "2020-2-2");

//                    }
        course[0].teacherPhone = "1066666655";


//                } else {
//                    classIconFile = new File(Environment.getExternalStorageDirectory() + "/daoyun/"
//                            + classIcon);
//                    if (!classIconFile.exists()) {
//                        final File finalClassIconFile = classIconFile;
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                OkHttpClient okHttpClient1 = new OkHttpClient();
//                                RequestBody requestBody1 = new FormBody.Builder()
//                                        .add("type", "classicon")
//                                        .add("icon", classIcon)
//                                        .build();
//                                Request request1 = new Request.Builder()
//                                        .url("http://47.98.236.0:8080/downloadicon")
//                                        .post(requestBody1)
//                                        .build();
//                                okHttpClient1.newCall(request1).enqueue(new Callback() {
//                                    @Override
//                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
//
//                                    }
//
//                                    @Override
//                                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                                        FileOutputStream os = new FileOutputStream(finalClassIconFile);
//                                        byte[] BytesArray = response.body().bytes();
//                                        os.write(BytesArray);
//                                        os.flush();
//                                        os.close();
//                                        if (teacherName == null) {
//                                            course[0] = new Course(finalClassIconFile.getAbsolutePath(), className, "", gradeClass, classId);
//                                        } else {
//                                            course[0] = new Course(finalClassIconFile.getAbsolutePath(), className, teacherName, gradeClass, classId);
//                                        }
//                                        course[0].teacherPhone = teacherPhone;
//                                    }
//                                });
//                            }
//                        }).start();
//                    } else {
//                        course[0] = new Course(classIconFile.getAbsolutePath(), className, teacherName, gradeClass, classId);
//                        course[0].teacherPhone = teacherPhone;
//                    }
//                }
        cList.add(course[0]);
        course[0] = new Course(R.drawable.course_img_2, "工程训练2", "陈大哥", "2班", "567", "2021-1-2");
        course[0].teacherPhone = "1234567";
        cList.add(course[0]);
//            }
//            Log.i("LoginInfo", cList.size() + "");
        return cList;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
    }
}
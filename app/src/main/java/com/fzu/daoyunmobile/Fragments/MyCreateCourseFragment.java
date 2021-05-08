package com.fzu.daoyunmobile.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.fzu.daoyunmobile.Activities.ClassTabActivity;
import com.fzu.daoyunmobile.Activities.CreateClassActivity;
import com.fzu.daoyunmobile.Adapter.MyCreateCourseAdapter;
import com.fzu.daoyunmobile.Adapter.MyJoinCourseAdapter;
import com.fzu.daoyunmobile.Entity.Course;
import com.fzu.daoyunmobile.R;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
//        progressDialog = new ProgressDialog(getContext());
//        progressDialog.setMessage("加载中...");
//        progressDialog.setCancelable(true);
//        progressDialog.show();
        initCourses();
        adapter = new MyCreateCourseAdapter(getContext(), R.layout.mycreatecourse_frameitem_layout, courseList, 2);
        listView = getActivity().findViewById(R.id.mycreatecourselist_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course = courseList.get(position);
                Intent intent = new Intent(getContext(), ClassTabActivity.class);
                intent.putExtra("courseName", course.getCourseName());
                intent.putExtra("classId", course.getClassId());
                intent.putExtra("enterType", "create");
                startActivity(intent);
            }
        });
    }

    private void initCourses() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                OkHttpClient okHttpClient = new OkHttpClient();
//                RequestBody requestBody = new FormBody.Builder()
//                        .add("phone", MainActivity.phoneNumber)
//                        .build();
//                Request request = new Request.Builder()
//                        .url("http://47.98.236.0:8080/mycreateclass")
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
//                        final List<Course> temp_courseList = parseJsonWithJsonObject(responseBodyStr);
////                        Log.i("myCreateFragInfo", courseList.get(0).getCourseName());
//                        afterAction(temp_courseList);
//
//                    }
//         });
            }
        }).start();
//        if(MainActivity.userName.equals("teacher")){
        Course course_1 = new Course(R.drawable.course_img_1, "工程实践", "池芝标", "20级电子信息", "567");
        courseList.add(course_1);
        Course course_2 = new Course(R.drawable.course_img_2, "工程训练", "池芝标", "20级电子信息", "567");
        courseList.add(course_2);
//
//        }
    }

    private void afterAction(final List<Course> temp_courseList) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                courseList = temp_courseList;
                adapter.notifyDataSetChanged();
                adapter = new MyCreateCourseAdapter(getContext(), R.layout.mycreatecourse_frameitem_layout, courseList, 2);
                listView.setAdapter(adapter);
                progressDialog.dismiss();
            }
        });
    }

    private List<Course> parseJsonWithJsonObject(String jsonData) throws IOException {
//        File classFile = new File(Environment.getExternalStorageDirectory() + "/daoyun/"
//                + MainActivity.phoneNumber + ".json");
//        if (classFile.exists()) {
//            classFile.delete();
//        }
//        classFile.createNewFile();
//        byte[] bt = new byte[4096];
//        bt = jsonData.getBytes();
//        FileOutputStream out = new FileOutputStream(classFile);
//        out.write(bt, 0, bt.length);
//        out.close();
//        try {
//            JSONArray jsonArray = new JSONArray(jsonData);
//            Log.i("myCreateFragInfo", jsonData);
//            final List<Course> cList = new ArrayList<Course>();
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                final String classId = jsonObject.getString("classId");
//                final String className = jsonObject.getString("className");
//                final String gradeClass = jsonObject.getString("gradeClass");
//                final String path = jsonObject.getString("classIcon");
//                final Course course;
//                if (path.equals("")) {
//                    if (MainActivity.name == null) {
//                        course = new Course(R.drawable.course_img_1, className, "", gradeClass, classId);
//                    } else {
//                        course = new Course(R.drawable.course_img_1, className, MainActivity.name, gradeClass, classId);
//                    }
//                    cList.add(course);
//                } else {
//                    final File imgFile = new File(Environment.getExternalStorageDirectory() + "/daoyun/" + path);
//                    if (!imgFile.exists()) {
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                OkHttpClient okHttpClient = new OkHttpClient();
//                                RequestBody requestBody = new FormBody.Builder()
//                                        .add("icon", path)
//                                        .add("type", "classicon")
//                                        .build();
//                                Request request = new Request.Builder()
//                                        .url("http://47.98.236.0:8080/downloadicon")
//                                        .post(requestBody)
//                                        .build();
//                                okHttpClient.newCall(request).enqueue(new Callback() {
//                                    @Override
//                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
//
//                                    }
//
//                                    @Override
//                                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                                        File iconFile = new File(Environment.getExternalStorageDirectory() + "/daoyun/" + path);
//                                        FileOutputStream os = new FileOutputStream(iconFile);
//                                        byte[] BytesArray = response.body().bytes();
//                                        os.write(BytesArray);
//                                        os.flush();
//                                        os.close();
//                                        Course course1;
//                                        if (MainActivity.name == null) {
//                                            course1 = new Course(iconFile.getAbsolutePath(), className, "", gradeClass, classId);
//                                        } else {
//                                            course1 = new Course(iconFile.getAbsolutePath(), className, MainActivity.name, gradeClass, classId);
//                                        }
//                                        cList.add(course1);
//                                    }
//                                });
//                            }
//                        }).start();
//                    } else {
//                        if (MainActivity.name == null) {
//                            course = new Course(imgFile.getAbsolutePath(), className, "", gradeClass, classId);
//                        } else {
//                            course = new Course(imgFile.getAbsolutePath(), className, MainActivity.name, gradeClass, classId);
//                        }
//                        cList.add(course);
//                    }
//                }
//
//            }
//            Log.i("LoginInfo", cList.size() + "");
//            return cList;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return null;
    }

}
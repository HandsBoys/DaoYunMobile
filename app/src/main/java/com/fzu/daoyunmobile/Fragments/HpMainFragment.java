package com.fzu.daoyunmobile.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.fzu.daoyunmobile.Activities.CreateClassActivity;
import com.fzu.daoyunmobile.Activities.MainActivity;
import com.fzu.daoyunmobile.Adapter.MyCreateCourseAdapter;
import com.fzu.daoyunmobile.Configs.GlobalConfig;
import com.fzu.daoyunmobile.Configs.RequestCodeConfig;
import com.fzu.daoyunmobile.Configs.UrlConfig;
import com.fzu.daoyunmobile.Entity.Course;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;
import com.fzu.daoyunmobile.Utils.HttpUtils.OkHttpUtil;
import com.google.zxing.activity.CaptureActivity;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * 主页面视图 Page
 */
public class HpMainFragment extends Fragment {

    protected TextView addTV;
    protected TextView myCreateTV;
    protected TextView myJoinTV;
    protected View myCreateView;
    protected View myJoinView;
    protected MyCreateCourseFragment myCreateFragment = new MyCreateCourseFragment();
    protected MyJoinCourseFragment myJoinFragment = new MyJoinCourseFragment();

    public HpMainFragment() {        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hp_main, null);
        //添加按钮
        addTV = view.findViewById(R.id.toolbar_right_tv);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final AppCompatActivity activity = (AppCompatActivity) getActivity();

        //俩块添加的视图
        myCreateView = activity.findViewById(R.id.view_mycreate);
        myJoinView = activity.findViewById(R.id.view_myjoin);
        myCreateTV = activity.findViewById(R.id.myCreateTv);
        myJoinTV = activity.findViewById(R.id.joinedClassTv);

        myJoinTV.setTextColor(Color.parseColor("#ff00bfff"));
        myCreateView.setVisibility(View.INVISIBLE);

        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_content_layout, myCreateFragment)
                .add(R.id.container_content_layout, myJoinFragment)
                .hide(myCreateFragment)
                .commit();

        addTV.setEnabled(true);
        addTV.setOnClickListener(v -> {
            showPopupMenu(addTV);
        });

        myJoinTV.setOnClickListener(v -> {
            myJoinTV.setTextColor(Color.parseColor("#ff00bfff"));
            myCreateTV.setTextColor(Color.parseColor("#80000000"));
            myJoinView.setVisibility(View.VISIBLE);
            myCreateView.setVisibility(View.INVISIBLE);
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .show(myJoinFragment)
                    .hide(myCreateFragment)
                    .commit();
        });

        myCreateTV.setOnClickListener(v -> {
            myCreateTV.setTextColor(Color.parseColor("#ff00bfff"));
            myJoinTV.setTextColor(Color.parseColor("#80000000"));
            myCreateView.setVisibility(View.VISIBLE);
            myJoinView.setVisibility(View.INVISIBLE);
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .show(myCreateFragment)
                    .hide(myJoinFragment)
                    .commit();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("HPMAINGETACT");
        Log.i("MainFragInfo", requestCode + " " + resultCode + " " + "gradeClass");
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK) {
//            BitmapDrawable bitmapDrawable = getImageDrawable(data.getStringExtra("classIcon"));
//            Course course = new Course(R.drawable.course_img_1, data.getStringExtra("className"),
//                    MainActivity.name, data.getStringExtra("gradeClass"));
//            MyCreateFragment.courseList.add(course);
            String classId = data.getStringExtra("classId");
            String className = data.getStringExtra("className");
            String gradeClass = data.getStringExtra("gradeClass");
            myCreateTV.setTextColor(Color.parseColor("#ff00bfff"));
            myJoinTV.setTextColor(Color.parseColor("#80000000"));
            myCreateView.setVisibility(View.VISIBLE);
            myJoinView.setVisibility(View.INVISIBLE);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .show(myCreateFragment)
                    .hide(myJoinFragment)
                    .commit();
                myCreateFragment.courseList.add(new Course(className, GlobalConfig.getNickName(), gradeClass, classId));
//                myCreateFrment.adapter.notifyDataSetChanged();

            myCreateFragment.adapter.notifyDataSetChanged();
            myCreateFragment.adapter = new MyCreateCourseAdapter(getContext(), R.layout.item_mycreatecourse
                    , myCreateFragment.courseList);
            myCreateFragment.listView.setAdapter(myCreateFragment.adapter);
        }
    }

    //添加选择按钮框
    private void showPopupMenu(View view) {
        //
        new XPopup.Builder(getContext())
                .isDarkTheme(true)
                .hasShadowBg(true)
//                            .hasBlurBg(true)
//                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .asBottomList("", new String[]{"创建班课", "使用班课号加入班课", "使用二维码加入班课"},
                        (position, text) -> {
                            switch (position) {
                                case 0:
                                    if (GlobalConfig.getIsTeacher())
                                        getActivity().startActivityForResult(new Intent(getContext(), CreateClassActivity.class), RequestCodeConfig.getCreateCourse());
                                    else {
                                        AlertDialogUtil.showConfirmClickAlertDialog("只有老师才能创建班课", getActivity());
                                    }
                                    break;
                                case 1:
                                    final EditText editText = new EditText(getContext());
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                                            .setTitle("请输入班课号")
                                            .setView(editText);
                                    builder.setPositiveButton("确定", (dialog, which) -> {
                                        String classStr = editText.getText().toString();
                                        searchClass(classStr);
                                    });
                                    builder.setNegativeButton("取消", null);
                                    builder.show();
                                    break;
                                case 2:
                                default:
                                    Intent intent = new Intent(getContext(), CaptureActivity.class);
                                    //必须要通过父activity下的forresult才能使接收和输入相同的code
                                    getActivity().startActivityForResult(intent, RequestCodeConfig.getScanqrCode());
                                    break;
                            }
                        }).show();
    }

    //加入班课之前搜索班级
    private void searchClass(final String classStr) {
        //获取用户信息
        OkHttpUtil.getInstance().GetWithToken(UrlConfig.getUrl(UrlConfig.UrlType.GET_COURSE_INFO) + classStr, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("FUCK GETUSERINFO Error" + e.getMessage());
                AlertDialogUtil.showToastText(e.getMessage(), getActivity());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String responseBodyStr = response.body().string();
                    if (responseBodyStr.length() < 5 || responseBodyStr.contains("班课获取失败") || responseBodyStr.contains("班课不存在")) {
                        AlertDialogUtil.showConfirmClickAlertDialog("班课不存在", getActivity());
                    } else {
                        JSONObject jsonObject = JSONObject.parseObject(responseBodyStr).getJSONObject("data");
                        final String teacherID = jsonObject.getString("teacherId");
                        if (teacherID.equals(GlobalConfig.getUserID())) {
                            AlertDialogUtil.showConfirmClickAlertDialog("不能加入自己创建的班课", getActivity());
                        } else {
                            final String finish = jsonObject.getString("finish");
                            if (finish.equals("true")) {
                                AlertDialogUtil.showConfirmClickAlertDialog("班课已结束", getActivity());
                            } else {
                                final String enableJoin = jsonObject.getString("enableJoin");
                                if (enableJoin.equals("false")) {
                                    AlertDialogUtil.showConfirmClickAlertDialog("班课不允许加入", getActivity());
                                } else {
                                    final String className = jsonObject.getJSONObject("classDto").getString("className");
                                    final String courseName = jsonObject.getString("courseName");
                                    AlertDialogUtil.showConfirmClickAlertDialogTwoButtonWithLister("确定加入" + courseName + className, getActivity(), (dialog, i) -> {
                                        joinClass(classStr);
                                    });
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    //获取不到用户信息则取消登陆 需要重新登陆
                    AlertDialogUtil.showConfirmClickAlertDialog("加入班课失败请重试", getActivity());
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    //加入班级
    private void joinClass(final String classStr) {

        //获取用户信息
        OkHttpUtil.getInstance().PutWithJsonToken(UrlConfig.getUrl(UrlConfig.UrlType.JOIN_COURSE) + "?courseId=" + classStr, new JSONObject(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("FUCK GETUSERINFO Error" + e.getMessage());
                AlertDialogUtil.showToastText(e.getMessage(), getActivity());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    String responseBodyStr = response.body().string();
                    if (responseBodyStr.contains("已经加入班课")) {
                        AlertDialogUtil.showConfirmClickAlertDialog("已经加入该班课!", getActivity());
                    } else if (responseBodyStr.contains("操作成功")) {
                        myJoinFragment.initCourses();
                        AlertDialogUtil.showConfirmClickAlertDialog("加入班课成功!", getActivity());
                    } else {
                        AlertDialogUtil.showConfirmClickAlertDialog(responseBodyStr, getActivity());
                    }
                } catch (Exception e) {
                    //获取不到用户信息则取消登陆 需要重新登陆
                    AlertDialogUtil.showConfirmClickAlertDialog("加入班课失败请重试", getActivity());
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    //扫描二维码
    public void onScanQRCode(String result) {
        searchClass(result);
    }

    //创建班课成功后
    public void onCreateCourese() {
        myCreateFragment.initCourses();
    }
}
package com.fzu.daoyunmobile.Fragments;

import android.content.DialogInterface;
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
    private final static int SCAN_CODE = 1028;

    public HpMainFragment() {        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
            Log.i("MainFragmentInfo", "add textview");
//                Toast.makeText(getContext(), "添加被按下", Toast.LENGTH_SHORT).show();

            showPopupMenu(addTV);
        });

//        add_btn = (Button) activity.findViewById(R.id.toolbar_right_btn);
//        add_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPopupMenu(add_btn);
//            }
//        });

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
        Log.i("MainFragInfo", requestCode + " " + resultCode + " " + "gradeClass");
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK) {
//            BitmapDrawable bitmapDrawable = getImageDrawable(data.getStringExtra("classIcon"));
//            Course course = new Course(R.drawable.course_img_1, data.getStringExtra("className"),
//                    MainActivity.name, data.getStringExtra("gradeClass"));
//            MyCreateFragment.courseList.add(course);
            String classId = data.getStringExtra("classId");
            String classIcon = data.getStringExtra("classIcon");
            String className = data.getStringExtra("className");
            String gradeClass = data.getStringExtra("gradeClass");
            Log.i("MainFragInfo", classIcon + " " + className + " " + gradeClass);
            myCreateTV.setTextColor(Color.parseColor("#ff00bfff"));
            myJoinTV.setTextColor(Color.parseColor("#80000000"));
            myCreateView.setVisibility(View.VISIBLE);
            myJoinView.setVisibility(View.INVISIBLE);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .show(myCreateFragment)
                    .hide(myJoinFragment)
                    .commit();
            if (classIcon.equals("")) {
                myCreateFragment.courseList.add(new Course(R.drawable.course_img_1, className, MainActivity.name, gradeClass, classId));
//                myCreateFragment.adapter.notifyDataSetChanged();
            } else {
                myCreateFragment.courseList.add(new Course(classIcon, className, MainActivity.name, gradeClass, classId));
//                myCreateFragment.adapter.notifyDataSetChanged();
            }
            myCreateFragment.adapter.notifyDataSetChanged();
            myCreateFragment.adapter = new MyCreateCourseAdapter(getContext(), R.layout.mycreatecourse_frameitem_layout
                    , myCreateFragment.courseList, 2);
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
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                //TODO 这里接入转换接口
                                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                                switch (position) {
                                    case 0:
                                        startActivityForResult(new Intent(getContext(), CreateClassActivity.class), 1);
                                        break;
                                    case 1:
                                        final EditText editText = new EditText(getContext());
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                                                .setTitle("请输入班课号")
                                                .setView(editText);
                                        builder.setPositiveButton("确定", (dialog, which) -> {
                                            String classStr = editText.getText().toString();
                                            joinClass(classStr);
                                        });
                                        builder.setNegativeButton("取消", null);
                                        builder.show();
                                        break;
                                    case 2:
                                    default:
                                        Intent intent = new Intent(getContext(), CaptureActivity.class);
                                        //必须要通过父activity下的forresult才能使接收和输入相同的code
                                        getActivity().startActivityForResult(intent, SCAN_CODE);
                                        break;
                                }
                            }
                        }).show();
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
                    AlertDialogUtil.showToastText(responseBodyStr, getActivity());
                    if (responseBodyStr.contains("操作成功")) {
                        myJoinFragment.initCourses();
                        AlertDialogUtil.showConfirmClickAlertDialog("加入班课成功!", getActivity());
                    } else {
                        AlertDialogUtil.showConfirmClickAlertDialog(responseBodyStr, getActivity());
                    }
                } catch (Exception e) {
                    //获取不到用户信息则取消登陆 需要重新登陆
                    AlertDialogUtil.showToastText(e.getMessage(), getActivity());
                    AlertDialogUtil.showConfirmClickAlertDialog("加入班课失败请重试", getActivity());
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    //扫描二维码
    public void onScanQRCode(String result) {
        joinClass(result);
    }
}
package com.fzu.daoyunmobile.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.fzu.daoyunmobile.Activities.ClassTabActivity;
import com.fzu.daoyunmobile.Configs.GlobalConfig;
import com.fzu.daoyunmobile.Configs.UrlConfig;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;
import com.fzu.daoyunmobile.Utils.HttpUtils.OkHttpUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {


    private Button backBtn;
    private Button exitDismissBtn;
    private ImageView classIconIV;
    private TextView classNameTV;
    private TextView courseNameTV;
    private TextView teacherNameTv;
    private TextView termTV;
    private TextView schoolDepartmentTV;
    protected CheckBox permitaddClassCB;
    private boolean isStudent = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        //返回键设置
        backBtn = view.findViewById(R.id.toolbar_left_btn);
        backBtn.setOnClickListener(v -> getActivity().finish());
        classNameTV = view.findViewById(R.id.class_classname_Tv);
        courseNameTV = view.findViewById(R.id.class_coursename_Tv);
        termTV = view.findViewById(R.id.class_coursedate_Tv);
        schoolDepartmentTV = view.findViewById(R.id.cloud_school_Tv);
        permitaddClassCB = view.findViewById(R.id.cb_permit_addclass);
        courseNameTV.setText(ClassTabActivity.courseName);
        termTV.setText(ClassTabActivity.term);
        classNameTV.setText(ClassTabActivity.className);

        //结束班课内容
        exitDismissBtn = view.findViewById(R.id.exit_dismiss_btn);
        if (!ClassTabActivity.enterType.equals("Create")) {
            exitDismissBtn.setText("退出班课");
            view.findViewById(R.id.all_join_lineout).setVisibility(View.GONE);
            isStudent = true;
        } else {
            if (!ClassTabActivity.enableJoin.equals("true"))
                permitaddClassCB.setChecked(false);
        }
        exitDismissBtn.setOnClickListener(v -> {
            if (isStudent)
                quitClass();
            else
                finishClass(true);
        });
        permitaddClassCB.setOnCheckedChangeListener(this);

        return view;
    }


    public void onCheckedChanged(CompoundButton checkBox, boolean checked) {
        switch (checkBox.getId()) {
            case R.id.cb_permit_addclass:
                if (checked) {// 选中吃
                    setJoin(true);
                } else {
                    setJoin(false);
                }
                break;
            default:
                break;
        }
    }

    //设置加入班级
    private void setJoin(boolean tag) {
        String ej = tag ? "true" : "false";
        //获取用户信息
        OkHttpUtil.getInstance().PostWithJsonToken(UrlConfig.getUrl(UrlConfig.UrlType.SET_JOIN_COURSE) + "id=" + ClassTabActivity.classId + "&enableJoin=" + ej, new JSONObject(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String responseBodyStr = response.body().string();
                    AlertDialogUtil.showToastText("修改成功", getActivity());
                } catch (Exception e) {
                    //获取不到用户信息则取消登陆 需要重新登陆
                    System.out.println(e.getMessage());
                }
            }
        });
    }


    private void finishClass(boolean tag) {
        String ej = tag ? "true" : "false";
        //获取用户信息
        OkHttpUtil.getInstance().PostWithJsonToken(UrlConfig.getUrl(UrlConfig.UrlType.SET_FINISH_COURSE) + "id=" + ClassTabActivity.classId + "&finish=" + ej, new JSONObject(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String responseBodyStr = response.body().string();
                    AlertDialogUtil.showToastText("修改成功", getActivity());

                } catch (Exception e) {
                    //获取不到用户信息则取消登陆 需要重新登陆
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    private void quitClass() {
        String ej = "true";
        //获取用户信息
        OkHttpUtil.getInstance().DeleteWithJsonToken(UrlConfig.getUrl(UrlConfig.UrlType.STUDENT_QUIT_CLASS) + ClassTabActivity.classId + "&studentId=" + GlobalConfig.getUserID(), new JSONObject(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String responseBodyStr = response.body().string();
                    AlertDialogUtil.showToastText("退出修改成功", getActivity());
                    getActivity().finish();

                } catch (Exception e) {
                    //获取不到用户信息则取消登陆 需要重新登陆
                    System.out.println(e.getMessage());
                }
            }
        });
    }

}
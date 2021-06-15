package com.fzu.daoyunmobile.Fragments;

import android.content.Intent;
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
import com.fzu.daoyunmobile.Activities.CreateClassActivity;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        backBtn = view.findViewById(R.id.exit_dismiss_btn);
        exitDismissBtn = view.findViewById(R.id.exit_dismiss_btn);
        classNameTV = view.findViewById(R.id.class_classname_Tv);
        courseNameTV = view.findViewById(R.id.class_coursename_Tv);
        //teacherNameTv = view.findViewById(R.id.more_teacher_Tv);
        termTV = view.findViewById(R.id.class_coursedate_Tv);
        schoolDepartmentTV = view.findViewById(R.id.cloud_school_Tv);

        permitaddClassCB = view.findViewById(R.id.cb_permit_addclass);

        courseNameTV.setText(ClassTabActivity.courseName);
        termTV.setText(ClassTabActivity.term);
        classNameTV.setText(ClassTabActivity.className);


        permitaddClassCB.setOnCheckedChangeListener(this);

        backBtn.setOnClickListener(v -> finishclass(true));
        return view;
    }


    public void onCheckedChanged(CompoundButton checkBox, boolean checked) {
        // TODO Auto-generated method stub
        switch (checkBox.getId()) {
            case R.id.cb_permit_addclass:
                if (checked) {// 选中吃
                    AlertDialogUtil.showToastText("选中", getActivity());
                    setJoin(true);


                } else {
                    setJoin(false);
                    //like.remove("eat");
                    AlertDialogUtil.showToastText("未选中", getActivity());
                }
                break;
            default:
                break;
        }
    }

    private void setJoin(boolean tag) {
        String ej = tag ? "true" : "false";
        //获取用户信息
        OkHttpUtil.getInstance().PostWithJsonToken(UrlConfig.getUrl(UrlConfig.UrlType.SET_JOIN_COURSE) + "id=" + ClassTabActivity.classId + "&enableJoin=" + ej, new JSONObject(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("FUCK CreateCourse" + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String responseBodyStr = response.body().string();

                } catch (Exception e) {
                    //获取不到用户信息则取消登陆 需要重新登陆
                    AlertDialogUtil.showToastText(e.getMessage(), getActivity());
                    System.out.println(e.getMessage());
                }
            }
        });
    }


    private void finishclass(boolean tag) {
        String ej = tag ? "true" : "false";
        //获取用户信息
        OkHttpUtil.getInstance().PostWithJsonToken(UrlConfig.getUrl(UrlConfig.UrlType.SET_FINISH_COURSE) + "id=" + ClassTabActivity.classId + "&finish=" + ej, new JSONObject(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("FUCK CreateCourse" + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String responseBodyStr = response.body().string();
                    AlertDialogUtil.showToastText("修改成功", getActivity());

                } catch (Exception e) {
                    //获取不到用户信息则取消登陆 需要重新登陆
                    AlertDialogUtil.showToastText(e.getMessage(), getActivity());
                    System.out.println(e.getMessage());
                }
            }
        });
    }
}
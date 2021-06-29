package com.fzu.daoyunmobile.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fzu.daoyunmobile.Activities.LoginActivity;
import com.fzu.daoyunmobile.Activities.MainActivity;
import com.fzu.daoyunmobile.Activities.UserInfoSetActivity;
import com.fzu.daoyunmobile.Configs.GlobalConfig;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;

import java.io.File;

/**
 *
 */
public class MyInfoFragment extends Fragment {

    protected LinearLayout userInfoLayout;
    protected Button logoutBtn;
    public TextView userNameTV;
    public TextView userTelTV;
    public TextView userRoleTV;
    public TextView userDateTV;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_info, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        userDateTV = getActivity().findViewById(R.id.user_date_set);
        userDateTV.setText(GlobalConfig.getCreateTime());

        userNameTV = getActivity().findViewById(R.id.user_name_set);
        userNameTV.setText(GlobalConfig.getNickName());

        userRoleTV = getActivity().findViewById(R.id.user_role_set);
        userRoleTV.setText(GlobalConfig.getIsTeacher() ? "教师" : "学生");

        userTelTV = getActivity().findViewById(R.id.user_tel_set);
        userTelTV.setText(GlobalConfig.getUserPhone());


        userInfoLayout = getActivity().findViewById(R.id.layout_me_header);
        userInfoLayout.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), UserInfoSetActivity.class));
        });

        LinearLayout linearLayout = getActivity().findViewById(R.id.psd_set_line);
        linearLayout.setOnClickListener(v -> {
            final EditText editText = new EditText(getContext());
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                    .setTitle("输入更改的密码!")
                    .setView(editText);
            builder.setPositiveButton("确定", (dialog, which) -> {
                String psd = editText.getText().toString();
                //TODO 密码修改接口
                AlertDialogUtil.showToastText("密码修改成功!", getActivity());
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        });


        logoutBtn = getActivity().findViewById(R.id.user_logout_btn);
        logoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            MainActivity.isFirst = true;
            startActivity(intent);
        });

    }

}
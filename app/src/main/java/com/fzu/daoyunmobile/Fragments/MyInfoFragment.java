package com.fzu.daoyunmobile.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fzu.daoyunmobile.Activities.LoginActivity;
import com.fzu.daoyunmobile.Activities.MainActivity;
import com.fzu.daoyunmobile.Configs.GlobalConfig;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 */
public class MyInfoFragment extends Fragment {

    protected Button mBtnLogin;
    protected LinearLayout userInfoLayout;
    protected LinearLayout privacyLayout;
    protected Button logoutBtn;
    protected Button userSetBtn;
    public static File iconFile = null;
    private String path;

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
        userInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogUtil.showToastText("FUCKUUU",getActivity());

//                Intent intent = new Intent(getContext(), UserInfoActivity.class);
//                startActivityForResult(intent, 1);
            }
        });

        LinearLayout linearLayout = getActivity().findViewById(R.id.user_protocol_layout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), UserProtocolActivity.class);
//                startActivity(intent);
            }
        });

        privacyLayout = getActivity().findViewById(R.id.prvacy_layout);
        privacyLayout.setOnClickListener(v -> {
            //startActivity(new Intent(getContext(), PrivacyPolicyActivity.class));
        });

        logoutBtn = getActivity().findViewById(R.id.user_logout_btn);
        logoutBtn.setOnClickListener(v -> {
            MainActivity.userName = null;
            startActivity(new Intent(getContext(), LoginActivity.class));
        });

    }

}
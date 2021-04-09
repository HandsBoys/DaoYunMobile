package com.fzu.daoyunmobile.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fzu.daoyunmobile.FrameItems.InputFrameItem;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Activities.RegisterActivity;

/**
 * 密码登录视图
 */
public class PsdLoginFragment extends Fragment {


    //注册按钮
    private Button registerBtn;
    //登录按钮
    private Button loginBtn;
    //输入账号框
    private InputFrameItem input_mobilenum;
    //输入密码框
    private InputFrameItem intput_psd;


    public PsdLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerBtn = getActivity().findViewById(R.id.bt_loginpsd_register);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpToRegister();
            }
        });

        loginBtn = getActivity().findViewById(R.id.bt_loginpsd_submit);
        loginBtn.setOnClickListener(v -> Login());


        input_mobilenum = new InputFrameItem(getActivity().getWindow().getDecorView(), R.id.input_mobilenum_psdlogin, R.id.input_frameitem_editText, R.id.input_frameitem_img, R.drawable.ic_login_username, "手机号/账号/邮箱");
        intput_psd = new InputFrameItem(getActivity().getWindow().getDecorView(), R.id.input_psd, R.drawable.ic_login_password, "密码");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_psd_login, container, false);
    }

    private void JumpToRegister() {
        startActivity(new Intent(getActivity(), RegisterActivity.class));
    }

    private void Login() {
        System.out.println(input_mobilenum.GetEditText());
    }
}
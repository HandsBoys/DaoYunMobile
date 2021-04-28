package com.fzu.daoyunmobile.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alibaba.fastjson.JSONObject;
import com.fzu.daoyunmobile.Activities.MainActivity;
import com.fzu.daoyunmobile.Configs.UrlConfig;
import com.fzu.daoyunmobile.FrameItems.InputFrameItem;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Activities.RegisterActivity;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;
import com.fzu.daoyunmobile.Utils.HttpUtils.OkHttpUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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

        //绑定注册按钮
        registerBtn = getActivity().findViewById(R.id.bt_loginpsd_register);
        registerBtn.setOnClickListener(v -> jumpToRegister());

        //绑定登录按钮
        loginBtn = getActivity().findViewById(R.id.bt_loginpsd_submit);
        loginBtn.setOnClickListener(v -> login());

        //绑定输入框
        input_mobilenum = new InputFrameItem(getActivity().getWindow().getDecorView(), R.id.input_mobilenum_psdlogin, R.id.input_frameitem_editText, R.id.input_frameitem_img, R.drawable.ic_login_username, "手机号/账号/邮箱");
        intput_psd = new InputFrameItem(getActivity().getWindow().getDecorView(), R.id.input_psd, R.drawable.ic_login_password, "密码");
        intput_psd.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_psd_login, container, false);
    }

    //跳转到注册
    private void jumpToRegister() {
        startActivity(new Intent(getActivity(), RegisterActivity.class));
    }

    //登录操作
    private void login() {
        //发送Model
        JSONObject json = new JSONObject();
        json.put("phone", input_mobilenum.getEditTextStr());
        json.put("code", input_mobilenum.getEditTextStr());
        json.put("password", "1234");
        json.put("userName", "1234");

        OkHttpUtil.getInstance().PostWithJson(UrlConfig.getUrl(UrlConfig.UrlType.PSD_LOGIN), json, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("LoginInfo", e.getMessage());
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBodyStr = response.body().string();
                Log.i("LoginInfo", responseBodyStr);
                System.out.println(responseBodyStr);

                if (responseBodyStr.contains("登录成功")) {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                } else {
//                    System.out.println("用户不存在或者密码错误");
                    AlertDialogUtil.showConfirmClickAlertDialog("用户不存在或者密码错误", getActivity());
                }
            }
        });

    }
}

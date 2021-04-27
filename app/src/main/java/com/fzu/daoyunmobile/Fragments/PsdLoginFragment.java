package com.fzu.daoyunmobile.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fzu.daoyunmobile.Activities.MainActivity;
import com.fzu.daoyunmobile.FrameItems.InputFrameItem;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Activities.RegisterActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
        intput_psd.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                try {
                    json.put("userName", input_mobilenum.getEditTextStr());
                    json.put("password", intput_psd.getEditTextStr());
                    json.put("code", "1234");
                    json.put("phone", "1066666655");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));

                Request request = new Request.Builder()
                        .header("Content-Type", "application/json")
                        .url("http://1.15.31.156:8081/login3")
                        .post(requestBody)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
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
                            System.out.println("用户不存在或者密码错误");
                        }
                    }
                });

            }

        }).start();
    }


    protected void showAlertDialog(final String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage(msg)
                .setPositiveButton("确定", null);
        builder.show();
    }
}

package com.fzu.daoyunmobile.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.fzu.daoyunmobile.Activities.MainActivity;
import com.fzu.daoyunmobile.Configs.UrlConfig;
import com.fzu.daoyunmobile.FrameItems.InputFrameItem;
import com.fzu.daoyunmobile.FrameItems.InputVCodeFrameItem;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;
import com.fzu.daoyunmobile.Utils.HttpUtils.HttpUtil;
import com.fzu.daoyunmobile.Utils.HttpUtils.OkHttpUtil;
import com.fzu.daoyunmobile.Utils.VerifyUtil;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * 验证码登录视图
 */
public class CodeLoginFragment extends Fragment {

    //输入账号框
    private InputFrameItem input_mobilenum;
    //输入验证码框
    private InputVCodeFrameItem input_vericode;
    //登录按钮
    private Button loginBtn;
    //生成的验证码
    private int verificationCode;

    private String session;
    private TextView qqLogin;

    public CodeLoginFragment() {

        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 登录按钮设置
        loginBtn = getActivity().findViewById(R.id.bt_login_submit);
        loginBtn.setOnClickListener(v -> login());

        //绑定手机号框
        input_mobilenum = new InputFrameItem(getActivity().getWindow().getDecorView(), R.id.input_mobilenum, R.id.input_frameitem_editText, R.id.input_frameitem_img, R.drawable.ic_login_username, "手机号");
        //输入框
        input_vericode = new InputVCodeFrameItem(getActivity().getWindow().getDecorView(), R.id.input_vericode, R.drawable.ic_login_password);

        qqLogin = getActivity().findViewById(R.id.qq_login_btn);
        qqLogin.setOnClickListener(v -> {
            System.out.println("FUCKYOU");
        });

        input_vericode.getSubBtn().setOnClickListener(v -> {
            //startActivity(new Intent(getActivity(), ThirdLoginActivity.class));
            sendMessage();
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_code_login, container, false);
    }

    //发送短信消息
    private void sendMessage() {
        String phone = input_mobilenum.getEditTextStr();
        Log.i("phoneInfo", input_mobilenum.getEditTextStr());
        if (VerifyUtil.isChinaPhoneLegal(phone)) {
            //倒计时开启
            input_vericode.startBtnDownTime(60);

            HttpUtil.sendMessage(phone, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseBodyStr = response.body().string();
                    Headers headers = response.headers();
                    session = response.headers().get("Set-Cookie");
                    Log.i("LoginInfoPre", session);
                    session = session.substring(0, session.indexOf(";")).substring(11);
                    Log.i("LoginInfoLast", session);
                    Log.i("LoginInfo", responseBodyStr);
                }
            });

        } else {
            AlertDialogUtil.showConfirmClickAlertDialog("请输入正确的手机号", getActivity());
        }

    }

    //登录
    private void login() {
        String phone = input_mobilenum.getEditTextStr();
        String vcode = input_vericode.getEditText();

        if (VerifyUtil.isChinaPhoneLegal(phone)) {
            // 创建传输Model
            JSONObject json = new JSONObject();
            json.put("phone", phone);
            json.put("code", vcode);
            json.put("password", "1234");
            json.put("userName", "1234");

            OkHttpUtil.getInstance().PostWithJson(UrlConfig.getUrl(UrlConfig.UrlType.CODE_LOGIN), json, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("LoginInfo", e.getMessage());
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseBodyStr = response.body().string();
                    Log.i("LoginInfo", responseBodyStr);
                    Headers headers = response.headers();
                    session = response.headers().get("Set-Cookie");
                    Log.i("LoginInfoAfter", session);

                    if (responseBodyStr.contains("登录成功")) {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    } else {
                        AlertDialogUtil.showConfirmClickAlertDialog("验证码错误", getActivity());
                        //showAlertDialog("用户不存在或者密码错误");
                        //System.out.println("验证码错误");
                    }
                }
            });

        } else {
            AlertDialogUtil.showConfirmClickAlertDialog("请输入正确的手机号", getActivity());
        }

//        String studentString ="{\"message\": \"Ok\",\"code\":200,\"data\":{\"captcha\":\"328551\"}}";
//
//        //JSON字符串转换成JSON对象
//        JSONObject messjsonObject = JSONObject.parseObject(studentString);
//
//        System.out.println(messjsonObject.get("data"));
//
//        System.out.println( messjsonObject.getJSONObject("data").getString("captcha"));

        //System.out.println("Login ");
        //startActivity(new Intent(getActivity(), QRCodeTestActivity.class));
        //startActivity(new Intent(getActivity(), MainActivity.class));
    }

    private void qqThirdLogin() {

    }
}
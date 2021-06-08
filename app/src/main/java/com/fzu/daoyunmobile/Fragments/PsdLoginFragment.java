package com.fzu.daoyunmobile.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.fzu.daoyunmobile.Activities.MainActivity;
import com.fzu.daoyunmobile.Configs.GlobalConfig;
import com.fzu.daoyunmobile.Configs.UrlConfig;
import com.fzu.daoyunmobile.FrameItems.InputFrameItem;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Activities.RegisterActivity;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;
import com.fzu.daoyunmobile.Utils.HttpUtils.OkHttpUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

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
    //验证码输入框
    private InputFrameItem input_vercode;
    //图片验证码
    private ImageView pic_vercode;
    //是否记住密码
    private CheckBox rememberUserCB;
    //忘记密码
    protected TextView forgetPasswordTV;

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
        //密码框
        intput_psd = new InputFrameItem(getActivity().getWindow().getDecorView(), R.id.input_psd, R.drawable.ic_login_password, "密码");
        intput_psd.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //验证码输入框
        input_vercode = new InputFrameItem(getActivity().getWindow().getDecorView(), R.id.input_vercode, R.drawable.ic_login_password, "验证码");

        //绑定图片验证码
        pic_vercode = getActivity().findViewById(R.id.pic_vercode);
        setPicVcode();
        pic_vercode.setOnClickListener(v -> setPicVcode());

        // 根据是否记住密码获取
        SharedPreferences preferences = getActivity().getSharedPreferences("remember_user", MODE_PRIVATE);
        if (!preferences.getString("userName", "").equals("") && !preferences.getString("password", "").equals("")) {
            input_mobilenum.getEditText().setText(preferences.getString("userName", ""));
            intput_psd.getEditText().setText(preferences.getString("password", ""));
        }
        rememberUserCB = getActivity().findViewById(R.id.cb_remember_login);
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
        json.put("code", input_vercode.getEditTextStr());
        json.put("password", intput_psd.getEditTextStr());
        json.put("userName", input_mobilenum.getEditTextStr());

        OkHttpUtil.getInstance().PostWithJson(UrlConfig.getUrl(UrlConfig.UrlType.PSD_LOGIN), json, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("LoginInfo", e.getMessage());
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBodyStr = response.body().string();

                if (responseBodyStr.contains("登陆成功") || responseBodyStr.contains("登录成功") || responseBodyStr.contains("token")) {

                    //存放账号密码
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("remember_user", MODE_PRIVATE).edit();
                    if (rememberUserCB.isChecked()) {
                        editor.putString("userName", input_mobilenum.getEditTextStr());
                        editor.putString("password", intput_psd.getEditTextStr());
                        editor.apply();
                    } else {
                        editor.putString("userName", "");
                        editor.putString("password", "");
                        editor.apply();
                    }

                    //JSON字符串转换成JSON对象
                    JSONObject messjsonObject = JSONObject.parseObject(responseBodyStr);

                    String token = messjsonObject.getJSONObject("data").getString("token");
                    //设置全局token
                    GlobalConfig.setUserToken(token);
                    getUserInfo();

                } else {
                    AlertDialogUtil.showConfirmClickAlertDialog("用户不存在或者密码错误", getActivity());
                }
            }
        });
    }

    //设置图片验证码
    private void setPicVcode() {
        OkHttpUtil.getInstance().PostWithJson(UrlConfig.getUrl(UrlConfig.UrlType.PIC_VERCODE), new JSONObject(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("LoginInfo", e.getMessage());
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //String responseBodyStr = response.body().string();
                //Log.i("CodeLoginInfo", responseBodyStr);
                InputStream inputStream = response.body().byteStream();//得到图片的流
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                try {
                    pic_vercode.setImageBitmap(bitmap);
                } catch (Exception e) {
                    //Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("PictureErro" + e.getMessage());
                }
            }
        });
    }

    private void getUserInfo() {
        //获取用户信息
        OkHttpUtil.getInstance().GetWithToken(UrlConfig.getUrl(UrlConfig.UrlType.USER_INFO), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("FUCK GETUSERINFO Error" + e.getMessage());
                AlertDialogUtil.showToastText(e.getMessage(), getActivity());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                try {
                    String responseBodyStr = response.body().string();
                    //JSON字符串转换成JSON对象
                    JSONObject messjsonObject = JSONObject.parseObject(responseBodyStr);
                    GlobalConfig.setUserID(messjsonObject.get("id").toString());
                    GlobalConfig.setUserPhone(messjsonObject.get("phone").toString());
                    GlobalConfig.setNickName(messjsonObject.get("nickName").toString());
                    GlobalConfig.setUserName(messjsonObject.get("userName").toString());
                    GlobalConfig.setSEX(messjsonObject.get("sex").toString());
                    startActivity(new Intent(getActivity(), MainActivity.class));
                } catch (Exception e) {
                    //获取不到用户信息则取消登陆 需要重新登陆
                    AlertDialogUtil.showToastText(e.getMessage(), getActivity());
                    AlertDialogUtil.showConfirmClickAlertDialog("网络超时请重新登陆", getActivity());
                }


            }
        });
    }
}


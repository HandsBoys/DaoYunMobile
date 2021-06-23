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
import com.fzu.daoyunmobile.Activities.ThirdLoginActivity;
import com.fzu.daoyunmobile.Configs.GlobalConfig;
import com.fzu.daoyunmobile.Configs.UrlConfig;
import com.fzu.daoyunmobile.FrameItems.InputFrameItem;
import com.fzu.daoyunmobile.FrameItems.InputVCodeFrameItem;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;
import com.fzu.daoyunmobile.Utils.HttpUtils.HttpUtil;
import com.fzu.daoyunmobile.Utils.HttpUtils.OkHttpUtil;
import com.fzu.daoyunmobile.Utils.VerifyUtil;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

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

    private TextView qqLogin;


    private static final String TAG = "ThirdLogin";
    private static final String APP_ID = "101950452";//官方获取的APPID
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;


    public CodeLoginFragment() {

        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // startActivity(new Intent(getActivity(), MainActivity.class));
        // 登录按钮设置
        loginBtn = getActivity().findViewById(R.id.bt_login_submit);
        loginBtn.setOnClickListener(v -> login());

        //获取腾讯第三方登录
        mTencent = Tencent.createInstance(APP_ID, getActivity().getApplicationContext());

        //绑定手机号框
        input_mobilenum = new InputFrameItem(getActivity().getWindow().getDecorView(), R.id.input_mobilenum, R.id.input_frameitem_editText, R.id.input_frameitem_img, R.drawable.ic_login_username, "手机号");
        //输入框
        input_vericode = new InputVCodeFrameItem(getActivity().getWindow().getDecorView(), R.id.input_vericode, R.drawable.ic_login_password);

        qqLogin = getActivity().findViewById(R.id.qq_login_btn);
        qqLogin.setOnClickListener(v -> {
            //startActivity(new Intent(getActivity(), ThirdLoginActivity.class));
            qqThirdLogin();
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
//                    Headers headers = response.headers();
//                    session = response.headers().get("Set-Cookie");
//                    Log.i("LoginInfoPre", session);
//                    session = session.substring(0, session.indexOf(";")).substring(11);
//                    Log.i("LoginInfoLast", session);
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
                    Log.i("CodeLoginInfo", responseBodyStr);

                    if (responseBodyStr.contains("登陆成功") || responseBodyStr.contains("登录成功") || responseBodyStr.contains("token")) {
                        JSONObject messjsonObject = JSONObject.parseObject(responseBodyStr);
//
//        System.out.println(messjsonObject.get("data"));
//
                        String token = messjsonObject.getJSONObject("data").getString("token");
                        //设置全局token
                        GlobalConfig.setUserToken(token);
                        getUserInfo();
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

        //startActivity(new Intent(getActivity(), QRCodeTestActivity.class));

    }

    //QQ第三方登陆
    private void qqThirdLogin() {
        mIUiListener = new BaseUiListener();
        //all表示获取所有权限
        //new Thread(() -> mTencent.login(getActivity(), "all", mIUiListener)).start();
        mTencent.login(getActivity(), "all", mIUiListener);
    }


    /**
     * 由上层Activity调用，获取第三方QQ登录的内容 由于Fragement无法读取
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onTencentCallBack(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
    }


    /**
     * 自定义监听器实现IUiListener接口后，需要实现的3个方法
     * onComplete完成 onError错误 onCancel取消
     */
    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            Toast.makeText(getActivity(), "授权成功", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "response:" + response);
            org.json.JSONObject obj = (org.json.JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getContext(), qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        Log.e(TAG, "登录成功" + response.toString());
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG, "登录失败" + uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG, "登录取消");
                    }

                    @Override
                    public void onWarning(int i) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(getActivity(), "授权失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getActivity(), "授权取消", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onWarning(int i) {
        }
    }

    //获取用户信息
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

                    AlertDialogUtil.showToastText(responseBodyStr, getActivity());
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
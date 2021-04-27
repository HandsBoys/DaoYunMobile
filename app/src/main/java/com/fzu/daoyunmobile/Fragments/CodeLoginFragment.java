package com.fzu.daoyunmobile.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.fzu.daoyunmobile.Activities.MainActivity;
import com.fzu.daoyunmobile.FrameItems.InputFrameItem;
import com.fzu.daoyunmobile.FrameItems.InputVCodeFrameItem;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.VerifyUtil;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
    private int seconds = 30;//秒数

    private String session;

    public CodeLoginFragment() {

        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 登录按钮设置
        loginBtn = getActivity().findViewById(R.id.bt_login_submit);
        loginBtn.setOnClickListener(v -> Login());


        input_mobilenum = new InputFrameItem(getActivity().getWindow().getDecorView(), R.id.input_mobilenum, R.id.input_frameitem_editText, R.id.input_frameitem_img, R.drawable.ic_login_username, "手机号/邮箱");
        input_vericode = new InputVCodeFrameItem(getActivity().getWindow().getDecorView(), R.id.input_vericode, R.drawable.ic_login_password);


        input_vericode.getSubBtn().setOnClickListener(v -> {
            //startActivity(new Intent(getActivity(), ThirdLoginActivity.class));
            // btnCountDownTimer.start();
            input_vericode.startBtnDownTime(60);
            // sendMessage();
//            Random r = new Random();
//            verificationCode = r.nextInt(899999) + 100000;
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
//                    .setTitle("验证码")
//                    .setMessage("验证码为：" + verificationCode)
//                    .setPositiveButton("确定", null);
//            builder.show();

        });
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            input_vericode.getSubBtn().setText(seconds <= 0 ? "重新获取" : String.format(Locale.CHINA, "%ds", seconds));
            input_vericode.getSubBtn().setEnabled(seconds <= 0);
            seconds--;
            if (seconds >= 0) {
                input_vericode.getSubBtn().postDelayed(this, 1000);//递归执行
            } else {
                seconds = 30;//复位
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_code_login, container, false);
    }

    private void sendMessage() {
        String phone = input_mobilenum.getEditTextStr();
        Log.i("phoneInfo", input_mobilenum.getEditTextStr());
        Pattern pattern = Pattern.compile("^[1]\\d{10}$");
        if (pattern.matcher(phone).matches()) {
            input_vericode.getSubBtn().setText("已发送");
            input_vericode.getSubBtn().setEnabled(false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    // MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");
                    String url = "http://1.15.31.156:8081/message?phone=" + phone;
                    Request request = new Request.Builder()
                            .url(url)
                            //.get(RequestBody.create(mediaType, requestBody))
                            .build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                                    Toast.makeText(RegisterActivity.this, "Connection failed!", Toast.LENGTH_SHORT).show();
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
                            //JSON字符串转换成JSON对象
//                        JSONObject messjsonObject = JSONObject.parseObject(responseBodyStr);
//
//                        System.out.println( messjsonObject.getJSONObject("data").getString("captcha"));
//                        // sendCodeAndPnone(messjsonObject.getJSONObject("data").getString("captcha"));
                            //sendCodeBtn.setText("已发送");
                            //sendCodeBtn.setEnabled(false);
                        }
                    });
                }
            }).start();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setMessage("请输入正确的手机号")
                    .setPositiveButton("确定", null);
            builder.show();
        }

    }

    //TODO 登录接口待做登录
    private void Login() {
        String phone = input_mobilenum.getEditTextStr();
        String vcode = input_vericode.getEditText();

        if (VerifyUtil.isChinaPhoneLegal(phone)) {
            new Thread(() -> {
                OkHttpClient okHttpClient = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                try {
                    json.put("phone", phone);
                    json.put("code", vcode);
                    json.put("password", "1234");
                    json.put("userName", "1234");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                Log.i("LoginInfo", session);
                Request request = new Request.Builder()
                        .header("Content-Type", "application/json")
                        .addHeader("Cookie", "Session=" + session)
                        .url("http://1.15.31.156:8081/login2")
                        .post(requestBody)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
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
                            //showAlertDialog("用户不存在或者密码错误");
                            System.out.println("验证码错误");
                        }
                    }
                });

            }).start();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setMessage("请输入正确的手机号")
                    .setPositiveButton("确定", null);
            builder.show();
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
}
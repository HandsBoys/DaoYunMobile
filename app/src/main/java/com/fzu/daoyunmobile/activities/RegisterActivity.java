
package com.fzu.daoyunmobile.Activities;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fzu.daoyunmobile.FrameItems.InputFrameItem;
import com.fzu.daoyunmobile.FrameItems.InputVCodeFrameItem;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.StatusBarUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    //验证码按钮
    private Button veriCodeBtn;
    //注册按钮
    private Button registerBtn;
    //返回按钮
    private Button backBtn;
    //生成的验证码
    private int verificationCode;

    private InputFrameItem input_mobilenum;
    private InputFrameItem intput_psd;
    private InputFrameItem intput_confpsd;
    private InputVCodeFrameItem input_vericode;
    private boolean tag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        StatusBarUtil.transparencyBar(RegisterActivity.this);


        registerBtn = findViewById(R.id.bt_register_submit);
        //TODO 注册按钮 后续接入API使用
        registerBtn.setOnClickListener(v -> {
            regiseter();
        });


        backBtn = findViewById(R.id.res_back_button);
        backBtn.setOnClickListener(v -> finish());


        input_mobilenum = new InputFrameItem(getWindow().getDecorView(), R.id.input_mobilenum, R.id.input_frameitem_editText, R.id.input_frameitem_img, R.drawable.ic_login_username, "手机号/邮箱");
        intput_psd = new InputFrameItem(getWindow().getDecorView(), R.id.input_psd, R.drawable.ic_login_password, "密码");
        intput_psd.GetEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);



        intput_confpsd = new InputFrameItem(getWindow().getDecorView(), R.id.input_confpsd, R.drawable.ic_login_password, "再次输入密码");
        intput_confpsd.GetEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        input_vericode = new InputVCodeFrameItem(getWindow().getDecorView(), R.id.input_vericode, R.drawable.ic_login_password);
        veriCodeBtn = input_vericode.GetSubBtn();

        veriCodeBtn.setOnClickListener(v -> {
            sendMessage();
            veriCodeBtn.setText("已发送");
            veriCodeBtn.setEnabled(false);
        });

    }

    private void sendMessage() {
        String phone = input_mobilenum.GetEditTextStr();
        Log.i("phoneInfo", input_mobilenum.GetEditTextStr());
        Pattern pattern = Pattern.compile("^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$");
        if (pattern.matcher(phone).matches()) {
            input_vericode.GetSubBtn().setText("已发送");
            input_vericode.GetSubBtn().setEnabled(false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient okHttpClient = new OkHttpClient();

                    verificationCode = (int) ((Math.random() * 9 + 1) * 100000);

                    // MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");
                    String url = "http://1.15.31.156:8081/message?phone=" + phone;
                    Request request = new Request.Builder()
                            .url(url)
                            //.get(RequestBody.create(mediaType, requestBody))
                            .build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            System.out.println(e.getMessage());
//                                    Toast.makeText(RegisterActivity.this, "Connection failed!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String responseBodyStr = response.body().string();
                            Log.i("RegisterInfo", responseBodyStr);
                        }
                    });
                }
            }).start();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage("请输入正确的手机号")
                    .setPositiveButton("确定", null);
            builder.show();
        }

    }

    private void regiseter() {
        System.out.println(intput_psd.GetEditTextStr() + intput_confpsd.GetEditTextStr());
        if (!intput_psd.GetEditTextStr().equals(intput_confpsd.GetEditTextStr())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage("密码不一致")
                    .setPositiveButton("确定", null);
            builder.show();
        } else {
            new Thread(() -> {
                OkHttpClient okHttpClient = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                System.out.println(input_mobilenum.GetEditTextStr());
                System.out.println(intput_psd.GetEditTextStr());
                System.out.println(input_vericode.GetEditText());
                System.out.println(input_mobilenum.GetEditTextStr());


                try {
                    json.put("code", input_vericode.GetEditText());
                    json.put("password", intput_psd.GetEditTextStr());
                    json.put("phone", input_mobilenum.GetEditTextStr());
                    json.put("userName", input_mobilenum.GetEditTextStr());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));

                Request request = new Request.Builder()
                        .header("Content-Type", "application/json")
                        .url("http://1.15.31.156:8081/sign2")
                        .post(requestBody)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.i("LoginInfo", e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String responseBodyStr = response.body().string();
                        Log.i("LoginInfo", responseBodyStr);
                        System.out.println(responseBodyStr);

                        if (responseBodyStr.contains("注册成功")) {
                            // alterDialog();
                            finish();
                        } else {
                            //System.out.println("用户不存在或者密码错误");
                        }
                    }
                });

            }).start();
        }
    }

    private void alterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("注册成功")
                .setPositiveButton("确定", (dialog, i) -> {
                    finish();
                });
        builder.show();
    }
}
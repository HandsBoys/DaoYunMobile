
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
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;
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
        // 设置密码格式
        intput_psd.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


        intput_confpsd = new InputFrameItem(getWindow().getDecorView(), R.id.input_confpsd, R.drawable.ic_login_password, "再次输入密码");
        intput_confpsd.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        input_vericode = new InputVCodeFrameItem(getWindow().getDecorView(), R.id.input_vericode, R.drawable.ic_login_password);
        veriCodeBtn = input_vericode.getSubBtn();

        veriCodeBtn.setOnClickListener(v -> {
            input_vericode.startBtnDownTime(60);
            sendMessage();
        });

    }

    private void sendMessage() {
        String phone = input_mobilenum.getEditTextStr();
        Log.i("phoneInfo", input_mobilenum.getEditTextStr());
        Pattern pattern = Pattern.compile("^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$");
        if (pattern.matcher(phone).matches()) {
            input_vericode.getSubBtn().setText("已发送");
            input_vericode.getSubBtn().setEnabled(false);
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
            AlertDialogUtil.showConfirmClickAlertDialog("请输入正确的手机号", this);
        }

    }

    //注册
    private void regiseter() {
        //判断密码是否一致
        if (!intput_psd.getEditTextStr().equals(intput_confpsd.getEditTextStr())) {
            AlertDialogUtil.showConfirmClickAlertDialog("俩次密码不一致", this);
        } else {
            new Thread(() -> {
                OkHttpClient okHttpClient = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                System.out.println(input_mobilenum.getEditTextStr());
                System.out.println(intput_psd.getEditTextStr());
                System.out.println(input_vericode.getEditText());
                System.out.println(input_mobilenum.getEditTextStr());


                try {
                    json.put("code", input_vericode.getEditText());
                    json.put("password", intput_psd.getEditTextStr());
                    json.put("phone", input_mobilenum.getEditTextStr());
                    json.put("userName", input_mobilenum.getEditTextStr());
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

package com.fzu.daoyunmobile.Activities;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.fzu.daoyunmobile.Configs.UrlConfig;
import com.fzu.daoyunmobile.FrameItems.InputFrameItem;
import com.fzu.daoyunmobile.FrameItems.InputVCodeFrameItem;
import com.fzu.daoyunmobile.R;
import com.fzu.daoyunmobile.Utils.AlertDialogUtil;
import com.fzu.daoyunmobile.Utils.HttpUtils.HttpUtil;
import com.fzu.daoyunmobile.Utils.HttpUtils.OkHttpUtil;
import com.fzu.daoyunmobile.Utils.StatusBarUtil;
import com.fzu.daoyunmobile.Utils.VerifyUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        StatusBarUtil.transparencyBar(RegisterActivity.this);


        registerBtn = findViewById(R.id.bt_register_submit);
        registerBtn.setOnClickListener(v -> {
            regiseter();
        });


        backBtn = findViewById(R.id.res_back_button);
        backBtn.setOnClickListener(v -> finish());


        input_mobilenum = new InputFrameItem(getWindow().getDecorView(), R.id.input_mobilenum, R.id.input_frameitem_editText, R.id.input_frameitem_img, R.drawable.ic_login_username, "手机号");
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
        if (VerifyUtil.isChinaPhoneLegal(phone)) {
            HttpUtil.sendMessage(phone, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println(e.getMessage());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseBodyStr = response.body().string();
                    Log.i("RegisterInfo", responseBodyStr);
                }
            });

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

            JSONObject json = new JSONObject();
            System.out.println(input_mobilenum.getEditTextStr());
            System.out.println(intput_psd.getEditTextStr());
            System.out.println(input_vericode.getEditText());
            System.out.println(input_mobilenum.getEditTextStr());
            json.put("code", input_vericode.getEditText());
            json.put("password", intput_psd.getEditTextStr());
            json.put("phone", input_mobilenum.getEditTextStr());
            json.put("userName", input_mobilenum.getEditTextStr());

            OkHttpUtil.getInstance().PostWithJson(UrlConfig.getUrl(UrlConfig.UrlType.QUiCK_REGISTER), json, (new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("LoginInfo", e.getMessage());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseBodyStr = response.body().string();
                    Log.i("LoginInfo", responseBodyStr);

                    if (responseBodyStr.contains("注册成功")) {
                        AlertDialogUtil.showConfirmClickAlertDialogWithLister("注册成功", RegisterActivity.this, (dialog, i) -> {
                            finish();
                        });
                        //finish();
                    } else {
                        System.out.println("用户不存在或者密码错误");
                    }
                }
            }));
        }
    }
}
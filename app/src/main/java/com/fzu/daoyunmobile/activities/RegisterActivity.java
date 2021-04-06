
package com.fzu.daoyunmobile.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.fzu.daoyunmobile.FrameItems.InputFrameItem;
import com.fzu.daoyunmobile.R;

import java.util.Random;

public class RegisterActivity extends AppCompatActivity {
    //验证码按钮
    private Button veriCodeBtn;
    //注册按钮
    private Button registerBtn;
    //账号
    private EditText userName;
    //密码
    private EditText password;
    //确认密码
    private EditText confPassword;
    //验证码
    private EditText veriCode;

    //生成的验证码
    private int verificationCode;

    private InputFrameItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        veriCodeBtn = findViewById(R.id.bt_veri_submit);
        userName = findViewById(R.id.et_login_username);
        password = findViewById(R.id.et_login_pwd);
        // confPassword = findViewById(R.id.tv_login_forget_pwd);
        veriCode = findViewById(R.id.et_reg_vericode);
        registerBtn = findViewById(R.id.bt_register_submit);

        item = new InputFrameItem(getWindow().getDecorView(), R.id.testID, R.drawable.ic_login_username);


        veriCodeBtn.setOnClickListener(v -> {
            Random r = new Random();
            verificationCode = r.nextInt(899999) + 100000;
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle("验证码")
                    .setMessage("验证码为：" + verificationCode)
                    .setPositiveButton("确定", null);
            builder.show();
            veriCodeBtn.setText("已发送");
            veriCodeBtn.setEnabled(false);
        });
        registerBtn.setOnClickListener(v -> finish());
    }
}